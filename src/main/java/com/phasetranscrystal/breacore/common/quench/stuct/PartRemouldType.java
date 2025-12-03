package com.phasetranscrystal.breacore.common.quench.stuct;

import com.phasetranscrystal.breacore.api.attribute.TriNum;
import com.phasetranscrystal.breacore.common.quench.IAttrElemProvider;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Map;

// 改进工艺可能可以要求一定数量的资源作为条件
public class PartRemouldType implements IAttrElemProvider {

    @Override
    public Map<Holder<Attribute>, TriNum> getAttributes() {
        return Map.of();
    }
}
