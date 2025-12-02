package com.phasetranscrystal.breacore.common.quench.perk;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.common.quench.BreaQuench;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * 装备词条组件
 */
public record EquipPerkComponent(int maxPerkWeight, int usedPerkWeight,
                                 Map<Perk, Double> perksAndStrength, Map<Perk, Double> enabledPerks) {
    public static final Codec<EquipPerkComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("max").forGetter(EquipPerkComponent::maxPerkWeight),
            Codec.unboundedMap(BreaRegistries.PERK.byNameCodec(), Codec.DOUBLE).fieldOf("pas").forGetter(EquipPerkComponent::perksAndStrength),
            BreaRegistries.PERK.byNameCodec().listOf().fieldOf("enabled").forGetter(EquipPerkComponent::getEnabledPerks)
    ).apply(i, EquipPerkComponent::create));

    public static EquipPerkComponent create(int maxPerkWeight, Map<Perk, Double> perksAndStrength, Collection<Perk> enabled) {
        ImmutableMap.Builder<Perk, Double> map = new ImmutableMap.Builder<>();
        for (Perk perk : enabled) {
            if (perksAndStrength.containsKey(perk))
                map.put(perk, perksAndStrength.get(perk));
        }
        ImmutableMap<Perk, Double> build = map.build();
        return new EquipPerkComponent(maxPerkWeight, build.keySet().stream().mapToInt(Perk::getPerkWeight).sum(), perksAndStrength, build);
    }

    public List<Perk> getEnabledPerks() {
        return List.copyOf(enabledPerks.keySet());
    }

    public Mutable toMutable() {
        return new Mutable(this);
    }

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

        public boolean canBuild() {
            return usedPerkWeight <= maxPerkWeight;
        }

        public EquipPerkComponent build() {
            if (!canBuild()) return null;
            return EquipPerkComponent.create(maxPerkWeight, ImmutableMap.copyOf(perksAndStrength), enabledPerks);
        }


        public record Result(int success, Set<Perk> extraModified, int usedWeight) {
            public static final Result FAILED = new Result(0, Collections.emptySet(), 0);
        }
    }


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
            provider.getPerkAndStrength().forEach((perk, strength) ->
                    allPerksAndStrength.merge(perk, strength, Double::sum)
            );
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
            Set<Perk> toRemove = new HashSet<>();

            // 找出所有非强制启用的词条
            for (Perk perk : enabledPerks) {
                if (!perk.forceEnable()) {
                    toRemove.add(perk);
                }
            }

            // 移除非强制启用的词条
            enabledPerks.removeAll(toRemove);
        }

        // 5. 创建新的组件实例并设置到stack中
        EquipPerkComponent newComponent = EquipPerkComponent.create(
                sumWeight,
                ImmutableMap.copyOf(allPerksAndStrength),
                enabledPerks
        );

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
        for (Perk dependency : perk.getDependencies()) {//检查依赖项
            if (!collected.contains(dependency)) {//如果依赖项未被处理过
                collected.add(dependency);//处理依赖项本身
                collectAllDependencies(dependency, collected);//递归处理依赖项的依赖项
            }
        }
    }

}
