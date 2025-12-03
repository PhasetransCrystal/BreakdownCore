package com.phasetranscrystal.breacore.common.quench.stuct;

import com.phasetranscrystal.breacore.api.attribute.IAttributeModifierProvider;
import com.phasetranscrystal.breacore.api.attribute.TriNum;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.common.quench.EquipAssemblyComponent;

import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class EquipType {

    public abstract Map<ResourceLocation, EquipAssemblySlot<?>> getSlots();

    public EquipmentSlot availableSlot;

    public boolean readyForAssembly(Map<ResourceLocation, Pair<PartRemouldType, ItemStack>> parts) {
        for (Map.Entry<ResourceLocation, EquipAssemblySlot<?>> entry : this.getSlots().entrySet()) {
            if (!entry.getValue().inMust()) continue;

            if (!parts.containsKey(entry.getKey()) && !matchType(parts.get(entry.getKey()).getSecond(), entry.getValue().partType()))
                return false;
        }
        return true;
    }

    public boolean doAssemblyIfReady(ItemStack target, Map<ResourceLocation, Pair<PartRemouldType, ItemStack>> parts) {
        // TODO 这里需要：检查所有必要格位是否填满 检查工艺改进所需资源 装配AssemblyResult并更新词条状态(移除已启用但已消除的词条)
        if (!readyForAssembly(parts)) return false;

        return true;
    }

    /**
     * 为锻造的装备生成attribute列表。
     *
     * @see EquipAssemblyComponent 锻造装备数据存储组件
     */
    public List<ItemAttributeModifiers.Entry> createEntries(EquipType.AssemblyResult slots, ItemStack stack) {
        Map<Holder<Attribute>, TriNum.Mutable> values = new HashMap<>();

        // 从各槽位中装配的物品上获取信息
        getSlots().forEach((rl, slot) -> {
            if (slots.parts.containsKey(rl)) {
                PartAndRemould g = slots.parts.get(rl);
                // 从材料信息拉取数值 需要附加slot的consumer
                if (g.remould != null) {
                    g.remould.mergeTo(values);
                }
            }
        });

        // 将信息统合为修饰器组
        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>();
        values.forEach((atr, tri) -> tri.build().createItemAttributeModifier(atr, EquipmentSlotGroup.bySlot(this.availableSlot), IAttributeModifierProvider.equipping(stack), entries));

        return ImmutableList.copyOf(entries);
    }

    public ResourceLocation getId() {
        return BreaRegistries.EQUIP_TYPE.getKey(this);
    }

    @Override
    public String toString() {
        return "EquipType(" + getId() + ")";
    }

    public record EquipAssemblySlot<T extends PartType>(T partType, boolean inMust,
                                                        BiFunction<Holder<Attribute>, TriNum, TriNum> valueMapper) {}

    public record AssemblyResult(EquipType type, Map<ResourceLocation, PartAndRemould> parts) {}

    public record PartAndRemould(Material material, @Nullable PartRemouldType remould) {}

    // TODO
    private static boolean matchType(ItemStack stack, PartType type) {
        return true;
    }
}
