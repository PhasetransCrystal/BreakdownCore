package com.phasetranscrystal.breacore.common.quench.perk;

import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.common.quench.BreaQuench;

import net.minecraft.world.item.ItemStack;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Math;

import java.util.*;

/**
 * 装备词条组件
 * <p>
 * 存储一件装备的最大词条质量，已消耗词条质量，可用词条质量与对应强度，以及启用的词条。
 * <p>
 * 主要起缓存作用。数据收集自实现了{@link IPerkElemProvider}接口的物品与物品组件。
 * <p>
 * 用于为{@link EntityPerkHandlerAttachment 实体词条处理器}提供物品的词条信息。
 * <p>
 * 应在物品重锻或词条状态变化时调用{@link EquipPerkComponent#update(ItemStack)}方法更新。
 *
 * @see ExtraPerkWeightComponent 为物品提供自定义额外词条质量的组件
 */
public record EquipPerkComponent(int maxPerkWeight, int usedPerkWeight,
                                 Map<Perk, Double> perksAndStrength, Map<Perk, Double> enabledPerks) {

    public static final Codec<EquipPerkComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("max").forGetter(EquipPerkComponent::maxPerkWeight),
            Codec.unboundedMap(BreaRegistries.PERK.byNameCodec(), Codec.DOUBLE).fieldOf("pas").forGetter(EquipPerkComponent::perksAndStrength),
            BreaRegistries.PERK.byNameCodec().listOf().fieldOf("enabled").forGetter(EquipPerkComponent::getEnabledPerks)).apply(i, EquipPerkComponent::create));

    /**
     * 装备词条组件序列化的信息只有最大词条比重，各词条强度以及启用的词条。
     * 考虑到在被调用时需要的信息，会在构造实例时进行额外处理。
     * <p>
     * 所有数据来源于收集其它组件或物品本体信息。此组件仅起缓存功能。
     */
    public static EquipPerkComponent create(int maxPerkWeight, Map<Perk, Double> perksAndStrength, Collection<Perk> enabled) {
        ImmutableMap.Builder<Perk, Double> map = new ImmutableMap.Builder<>();
        for (Perk perk : enabled) {
            if (perksAndStrength.containsKey(perk))
                map.put(perk, perksAndStrength.get(perk));
        }
        ImmutableMap<Perk, Double> build = map.build();
        return new EquipPerkComponent(maxPerkWeight, build.keySet().stream().mapToInt(Perk::getPerkWeight).sum(), perksAndStrength, build);
    }

    /**
     * 获取当前启用的词条
     */
    public List<Perk> getEnabledPerks() {
        return List.copyOf(enabledPerks.keySet());
    }

    /**
     * 将信息转换为一个可变对象
     * <p>
     * 可变对象更适合在对应的工作台中调整启用的词条。
     */
    public Mutable toMutable() {
        return new Mutable(this);
    }

    /**
     * 一个物品词条组件的可变版本。用于在对应的工作台中快捷处理词条的启用与禁用。
     */
    public static class Mutable {

        public static final Pair<Set<Perk>, Integer> EMPTY_PAIR = new Pair<>(Collections.emptySet(), 0);
        public final int maxPerkWeight;
        public final Map<Perk, Double> perksAndStrength;
        private int usedPerkWeight;
        private Set<Perk> enabledPerks;

        public Mutable(EquipPerkComponent component) {
            this.maxPerkWeight = component.maxPerkWeight;
            this.perksAndStrength = component.perksAndStrength;
            this.enabledPerks = new HashSet<>(component.enabledPerks.keySet());
            this.usedPerkWeight = component.usedPerkWeight;
        }

        public boolean canDisablePrecheck(Perk perk) {
            return enabledPerks.contains(perk) && !perk.forceEnable();
        }

        public boolean canEnablePrecheck(Perk perk) {
            return !enabledPerks.contains(perk) && perksAndStrength.containsKey(perk);
        }

        /**
         * 尝试禁用一个词条。这可能连锁禁用依赖于其的一系列词条。
         *
         * @param perk     尝试禁用的词条
         * @param simulate 是否为模拟 若为true则不会对实例造成实际更改
         */
        public Result disable(Perk perk, boolean simulate) {
            if (!canDisablePrecheck(perk)) return Result.FAILED;
            Set<Perk> enabled = simulate ? new HashSet<>(enabledPerks) : enabledPerks;
            enabled.remove(perk);
            Set<Perk> removed = checkPerkDependencies(enabled);
            int usedWeight = enabled.stream()
                    .mapToInt(Perk::getPerkWeight)
                    .sum();
            if (!simulate) usedPerkWeight = usedWeight;
            return new Result(1, removed, usedWeight);
        }

        /**
         * 尝试启用一个词条。这可能连锁启用依赖于其的一系列词条。
         *
         * @param perk     尝试启用的词条
         * @param simulate 是否为模拟 若为true则不会对实例造成实际更改
         */
        public Result enable(Perk perk, boolean simulate) {
            if (!canEnablePrecheck(perk)) return Result.FAILED;
            // 收集所有需要启用的perk依赖项
            Set<Perk> perksToEnable = new HashSet<>();
            perksToEnable.add(perk);
            collectAllDependencies(perk, perksToEnable);

            // 检查所有依赖项是否都存在
            Set<Perk> notExist = new HashSet<>(perksToEnable);
            notExist.removeAll(perksAndStrength.keySet());
            if (!notExist.isEmpty()) return new Result(-1, notExist, 0);

            // 过滤掉已经启用的perk
            perksToEnable.removeAll(enabledPerks);

            // 计算新的启用集合和权重
            Set<Perk> newEnabledPerks = simulate ? new HashSet<>(enabledPerks) : enabledPerks;
            newEnabledPerks.addAll(perksToEnable);

            // 计算新的权重
            int newUsedWeight = newEnabledPerks.stream()
                    .mapToInt(Perk::getPerkWeight)
                    .sum();
            if (!simulate) this.usedPerkWeight = newUsedWeight;

            return new Result(1, perksToEnable, newUsedWeight);
        }

        /**
         * 检查能否构造，即已使用的词条比重是否大于最大比重容量。
         */
        public boolean canBuild() {
            return usedPerkWeight <= maxPerkWeight;
        }

        /**
         * 检查构造并重新将可变组件变回固定组件。
         */
        public EquipPerkComponent build() {
            if (!canBuild()) return null;
            return EquipPerkComponent.create(maxPerkWeight, ImmutableMap.copyOf(perksAndStrength), enabledPerks);
        }

        /**
         * 修改结果。不保证占用的词条比重小等于最大比重。
         *
         * @param success       是否成功 0为前置判断不成立，1为成功，-1为存在依赖缺失。
         * @param extraModified 连带产生的修改(启用/禁用)。若success=-1则代表缺失的依赖。
         * @param usedWeight    在结束状态下消耗的词条比重量。
         */
        public record Result(int success, Set<Perk> extraModified, int usedWeight) {

            public static final Result FAILED = new Result(0, Collections.emptySet(), 0);
        }
    }

    /**
     * 更新物品堆的装备词条组件
     */
    public static void update(ItemStack stack) {
        EquipPerkComponent component = stack.get(BreaQuench.EQUIP_PERK_COMPONENT);
        List<IPerkElemProvider> providers = new ArrayList<>();
        if (stack.getItem() instanceof IPerkElemProvider p) {
            providers.add(p);
        }
        stack.getComponents().forEach(t -> {
            if (t.value() instanceof IPerkElemProvider p) providers.add(p);
        });

        if (component == null && providers.isEmpty()) return;

        // 1. 计算所有perk及对应强度（累加）
        Map<Perk, Double> allPerksAndStrength = new HashMap<>();
        int sumWeight = 0;

        for (IPerkElemProvider provider : providers) {
            // 累加额外权重
            sumWeight += provider.extraPerkWeight();

            // 累加词条强度
            provider.getPerkAndStrength().forEach((perk, strength) -> allPerksAndStrength.merge(perk, strength, (v1, v2) -> Math.clamp(0, perk.getMaxPerkStrength(), v1 + v2)));
        }

        // 2. 处理启用名单
        Set<Perk> enabledPerks = new HashSet<>();
        if (component != null) {
            enabledPerks.addAll(component.enabledPerks().keySet());
        }

        // 移除已消失的perk
        enabledPerks.removeIf(perk -> !allPerksAndStrength.containsKey(perk));

        // 添加强制启用的新增perk
        allPerksAndStrength.keySet().stream().filter(Perk::forceEnable).forEach(enabledPerks::add);

        // 3. 检查词条依赖（循环处理直到没有变化）
        checkPerkDependencies(enabledPerks);

        // 4. 计算剩余权重
        int usedWeight = enabledPerks.stream()
                .mapToInt(Perk::getPerkWeight)
                .sum();
        // 如果总权重超过上限，禁用所有非强制启用的词条
        if (usedWeight > sumWeight) {
            // 找出所有非强制启用的词条
            enabledPerks.removeIf(perk -> !perk.forceEnable());
        }

        // 5. 创建新的组件实例并设置到stack中
        EquipPerkComponent newComponent = EquipPerkComponent.create(
                sumWeight,
                ImmutableMap.copyOf(allPerksAndStrength),
                enabledPerks);

        stack.set(BreaQuench.EQUIP_PERK_COMPONENT, newComponent);
    }

    public static Set<Perk> checkPerkDependencies(Set<Perk> enabledPerks) {
        boolean changed;
        Set<Perk> influenced = new HashSet<>();
        do {
            changed = false;
            Set<Perk> toRemove = new HashSet<>();

            for (Perk perk : enabledPerks) {
                Set<Perk> dependencies = perk.getDependencies();
                // 检查依赖是否全部满足
                if (!enabledPerks.containsAll(dependencies)) {
                    toRemove.add(perk);
                    influenced.add(perk);
                    changed = true;
                }
            }

            enabledPerks.removeAll(toRemove);
        } while (changed);
        return influenced;
    }

    /**
     * 递归收集所有依赖项（包括传递依赖）
     */
    public static void collectAllDependencies(Perk perk, Set<Perk> collected) {
        for (Perk dependency : perk.getDependencies()) {// 检查依赖项
            if (!collected.contains(dependency)) {// 如果依赖项未被处理过
                collected.add(dependency);// 处理依赖项本身
                collectAllDependencies(dependency, collected);// 递归处理依赖项的依赖项
            }
        }
    }
}
