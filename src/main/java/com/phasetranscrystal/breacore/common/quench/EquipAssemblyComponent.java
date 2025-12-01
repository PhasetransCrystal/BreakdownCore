package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.breacore.api.attribute.IAttributeModifierProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

/**
 * 装备组装信息组件。内容只应在装备被重新锻造时发生变化
 *
 * @param type  装备类型 决定了装备的基本装配模型，即这是什么类型的装备。
 * @param slots 装备槽位 即该装备组装的每个槽位上装配的内容是什么，分为该部件的材料与该部件使用的改进工艺(可空)两部分。
 */
public record EquipAssemblyComponent(EquipType type,
                                     EquipType.AssemblyResult slots,
                                     List<ItemAttributeModifiers.Entry> entries) implements IAttributeModifierProvider {

    public EquipAssemblyComponent(EquipType type,
                                  EquipType.AssemblyResult slots,
                                  ItemStack itemStack) {
        this(type, slots, type.createEntries(slots, itemStack));
    }


    //TODO
    @Override
    public List<ItemAttributeModifiers.Entry> getEntries() {
        return entries;
    }
}
