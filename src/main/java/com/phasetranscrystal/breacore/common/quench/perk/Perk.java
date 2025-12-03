package com.phasetranscrystal.breacore.common.quench.perk;

import com.phasetranscrystal.breacore.api.attribute.TriNum;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.Event;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * 装备词条
 * <p>
 * 词条主要用于为装备提供一系列额外加成属性，或在装备后在某一事件触发时执行行为。
 * <p>
 * 按照目前设计，词条主要分为装备固有词条和材料词条。其中{@link EquipInherentPerk 装备固有词条}应为0比重且不具有词条依赖的。
 */
public abstract class Perk {

    /**
     * 词条最大强度
     * <p>
     * 词条强度越高，其表现出的能力应越强。
     * <p>
     * 在一件装备内，词条强度取各提供器之和；在所有装备中，词条强度取最大值。
     */
    public abstract double getMaxPerkStrength();

    /**
     * 词条提供的属性。
     * <p>
     * 词条强度 -> 各属性以及其三阶段对应的值
     */
    public abstract Map<Holder<Attribute>, TriNum> getAttributesByStrength(double strength);

    /**
     * 词条提供的事件监听器。
     * <p>
     * 由{@link EntityPerkHandlerAttachment 实体词条控制器}统一处理。
     * <p>
     * 事件类型 -> 消费器(事件实例，词条强度)
     *
     * @see com.phasetranscrystal.breacore.common.horiz.EventDistributor 实体事件调度器
     */
    public abstract Map<Class<? extends Event>, BiConsumer<Event, Double>> getEventConsumers();

    /**
     * 词条比重
     * <p>
     * 一件装备中启用的所有词条比重之和应小于最大词条比重之和
     */
    public abstract int getPerkWeight();

    /**
     * 是否为强制启用
     */
    public abstract boolean forceEnable();

    /**
     * 获取词条依赖
     * <p>
     * 只有词条依赖全部存在且启用是，词条才可以被启用。
     */
    public Set<Perk> getDependencies() {
        return Collections.emptySet();
    }

    public ResourceLocation getId() {
        return BreaRegistries.PERK.getKey(this);
    }

    @Override
    public String toString() {
        return "Perk(" + getId() + ")";
    }
}
