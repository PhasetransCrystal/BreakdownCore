package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.breacore.api.attribute.TriNum;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Map;

//改进工艺可能可以要求一定数量的资源作为条件
public class PartRemould implements IAttrElemProvider {

    @Override
    public Map<Holder<Attribute>, TriNum> getAttributes() {
        return Map.of();
    }

}
