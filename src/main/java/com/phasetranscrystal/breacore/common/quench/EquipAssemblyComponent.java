package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.breacore.api.attribute.IAttributeModifierProvider;
import com.phasetranscrystal.breacore.common.quench.perk.IPerkElemProvider;
import com.phasetranscrystal.breacore.common.quench.perk.Perk;
import com.phasetranscrystal.breacore.common.quench.stuct.EquipType;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Map;

/**
 * 装备组装信息组件。内容只应在装备被重新锻造时发生变化
 *
 * @param type  装备类型 决定了装备的基本装配模型，即这是什么类型的装备。
 * @param slots 装备槽位 即该装备组装的每个槽位上装配的内容是什么，分为该部件的材料与该部件使用的改进工艺(可空)两部分。
 */
public record EquipAssemblyComponent(EquipType type,
                                     EquipType.AssemblyResult slots,
                                     List<ItemAttributeModifiers.Entry> entries)
        implements IAttributeModifierProvider, IPerkElemProvider {

    public static final Codec<EquipAssemblyComponent> CODEC = Codec.unit(new EquipAssemblyComponent(null, null, List.of()));// TODO

    public EquipAssemblyComponent(EquipType type,
                                  EquipType.AssemblyResult slots,
                                  ItemStack itemStack) {
        this(type, slots, type.createEntries(slots, itemStack));
    }

    @Override
    public List<ItemAttributeModifiers.Entry> getEntries() {
        return entries;
    }

    @Override
    public Map<Perk, Double> getPerkAndStrength() {
        return Map.of();// 从材料系统拉取各部件材料的词条 强度为1
    }
}
