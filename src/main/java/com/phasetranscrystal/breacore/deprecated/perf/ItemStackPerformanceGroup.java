package com.phasetranscrystal.breacore.deprecated.perf;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import com.google.common.collect.*;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record ItemStackPerformanceGroup(Map<EquipmentSlotGroup, PerformancePack> stableData,
                                        Map<EquipmentSlotGroup, PerformancePack> activeData,
                                        Multimap<EquipmentSlotGroup, ItemAttributeModifiers.Entry> attributes) {

    public static final ItemStackPerformanceGroup EMPTY = new ItemStackPerformanceGroup(Map.of(), Map.of(), ImmutableMultimap.of());
    public static final Codec<ItemStackPerformanceGroup> CODEC_UNIT = Codec.unit(EMPTY);

    // 在LivingEquipmentChangeEvent事件内提起的更新任务：移除旧stack绑定 刷新新stack 绑定新stack

    // 当玩家装备没有切换物品，而是物品本身变化时被提起时，checkEquip = true
    public static ItemStackPerformanceGroup rebuild(@Nullable Entity entity, ItemStack stack, boolean checkEquip) {
        ItemStackPerformanceGroup oldGroup = null;// TODO GET FROM COMPONENTS

        EquipmentSlot slotToSet = null;
        if (checkEquip && entity instanceof LivingEntity living) {
            slotToSet = living.getEquipmentSlotForItem(stack);
        }

        // 通过位置过滤找到需要立刻变动的部分 若装备未被装备则始终为空
        List<PerformancePack> toRemove = new ArrayList<>();
        List<PerformancePack> toAttach = new ArrayList<>();

        Multimap<EquipmentSlotGroup, PerformancePack> stablePartMap = HashMultimap.create();
        Multimap<EquipmentSlotGroup, PerformancePack> activePartMap = HashMultimap.create();

        // 收集物品堆具有的所有组
        if (stack.getItem() instanceof IPerformancePackProvider provider) {
            provider.availableSlotGroups().forEach(
                    slotGroup -> Optional.ofNullable(provider.get(slotGroup))
                            .ifPresent(pack -> (provider.isStable() ? stablePartMap : activePartMap).put(slotGroup, pack)));
        }
        stack.getComponents().forEach(component -> {
            if (component.value() instanceof IPerformancePackProvider provider) {
                provider.availableSlotGroups().forEach(
                        slotGroup -> Optional.ofNullable(provider.get(slotGroup))
                                .ifPresent(pack -> (provider.isStable() ? stablePartMap : activePartMap).put(slotGroup, pack)));
            }
        });

        // 如果物品不具有任何表现包，直接返回
        if (stablePartMap.isEmpty() && activePartMap.isEmpty()) {
            return null;
        }

        // 创建新的Map用于存储结果
        Map<EquipmentSlotGroup, PerformancePack> newStableData = new HashMap<>();
        Map<EquipmentSlotGroup, PerformancePack> newActiveData = new HashMap<>();

        HashSet<EquipmentSlotGroup> attributeChanged = new HashSet<>();

        // 比较stable数据的差异
        attributeChanged.addAll(compareAndBuildPerformancePacks(oldGroup != null ? oldGroup.stableData() : null,
                stablePartMap, toRemove, toAttach, newStableData, true, slotToSet));

        // 比较active数据的差异
        attributeChanged.addAll(compareAndBuildPerformancePacks(oldGroup != null ? oldGroup.activeData() : null,
                activePartMap, toRemove, toAttach, newActiveData, false, slotToSet));

        if (entity != null) {
            toRemove.forEach(pack -> pack.debind(entity));
            toAttach.forEach(pack -> pack.binding(entity));
        }

        Multimap<EquipmentSlotGroup, ItemAttributeModifiers.Entry> fm;
        // 数据在ItemAttributeModifierEvent事件中附加

        // 重构attribute数据
        if (!attributeChanged.isEmpty()) {
            Multimap<EquipmentSlotGroup, ItemAttributeModifiers.Entry> map = oldGroup == null ? HashMultimap.create() : HashMultimap.create(oldGroup.attributes());
            attributeChanged.forEach(pose -> {
                map.removeAll(pose);
                if (newStableData.containsKey(pose))
                    map.putAll(pose,
                            newStableData.get(pose).createAttributes().stream()
                                    .map(pair -> new ItemAttributeModifiers.Entry(pair.key(), pair.value(), pose))
                                    .toList());
                if (newActiveData.containsKey(pose))
                    map.putAll(pose,
                            newActiveData.get(pose).createAttributes().stream()
                                    .map(pair -> new ItemAttributeModifiers.Entry(pair.key(), pair.value(), pose))
                                    .toList());
            });
            fm = ImmutableMultimap.copyOf(map);
        } else {
            fm = oldGroup == null ? ImmutableMultimap.of() : ImmutableMultimap.copyOf(oldGroup.attributes);
        }

        // 整合结果到新的ItemStackPerformanceGroup实例
        return new ItemStackPerformanceGroup(ImmutableMap.copyOf(newStableData), ImmutableMap.copyOf(newActiveData), fm);
    }

    /**
     * 比较新旧PerformancePack并构建新的PerformancePack
     */
    private static HashSet<EquipmentSlotGroup> compareAndBuildPerformancePacks(
                                                                               @Nullable Map<EquipmentSlotGroup, PerformancePack> oldData,
                                                                               Multimap<EquipmentSlotGroup, PerformancePack> newPartMap,
                                                                               List<PerformancePack> toRemove,
                                                                               List<PerformancePack> toAttach,
                                                                               Map<EquipmentSlotGroup, PerformancePack> resultData,
                                                                               boolean isStable,
                                                                               @Nullable EquipmentSlot currentSlot) {
        // 获取所有涉及的EquipmentSlotGroup（新旧合并）
        Set<EquipmentSlotGroup> allSlotGroups = new HashSet<>();
        if (oldData != null) {
            allSlotGroups.addAll(oldData.keySet());
        }
        allSlotGroups.addAll(newPartMap.keySet());

        HashSet<EquipmentSlotGroup> set = new HashSet<>();

        for (EquipmentSlotGroup slotGroup : allSlotGroups) {
            PerformancePack oldPack = oldData != null ? oldData.get(slotGroup) : null;
            Collection<PerformancePack> newPacks = newPartMap.get(slotGroup);

            // 计算新旧哈希值集合
            IntSet oldHashes = oldPack != null ? oldPack.elementHashes() : IntSets.emptySet();
            IntSet newHashes = calculateNewHashes(newPacks);

            boolean changeSlotFlag = currentSlot != null && slotGroup.test(currentSlot);

            // 检查是否有差异 有差异表示对于当前格位，数据有所变动
            if (!same(oldHashes, newHashes)) {
                set.add(slotGroup);
                // 原PerformancePack非空，添加到toRemove
                if (oldPack != null && changeSlotFlag) {
                    toRemove.add(oldPack);
                }

                // 新一组PerformancePack非空，构建新的PerformancePack
                if (!newPacks.isEmpty()) {
                    PerformancePack newPack = PerformancePack.GroupBuilder.of(PerformancePack.PATH_BY_SLOT.get(slotGroup, isStable), newPacks);
                    if (changeSlotFlag) {
                        toAttach.add(newPack);
                    }
                    resultData.put(slotGroup, newPack);
                }
            } else if (oldPack != null) {// 无差异，保留原有的PerformancePack
                resultData.put(slotGroup, oldPack);
            }
        }

        return set;
    }

    /**
     * 计算新PerformancePack集合中所有子元素的哈希值并集
     */
    private static IntSet calculateNewHashes(Collection<PerformancePack> newPacks) {
        IntSet newHashes = new IntOpenHashSet();
        for (PerformancePack pack : newPacks) {
            newHashes.addAll(pack.elementHashes());
        }
        return newHashes;
    }

    private static boolean same(IntSet set, IntSet set2) {
        if (set.size() != set2.size()) {
            return false;
        }
        IntSet set3 = new IntOpenHashSet();
        set3.addAll(set);
        set3.removeAll(set2);
        return set3.isEmpty();
    }
}
