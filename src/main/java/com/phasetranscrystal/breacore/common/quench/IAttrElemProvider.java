package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.breacore.api.attribute.TriNum;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Map;

public interface IAttrElemProvider {

    Map<Holder<Attribute>, TriNum> getAttributes();

    default void mergeTo(Map<Holder<Attribute>, TriNum.Mutable> map) {
        mergeTo(this, map);
    }

    static void mergeTo(IAttrElemProvider provider, Map<Holder<Attribute>, TriNum.Mutable> map) {
        provider.getAttributes().forEach((atr, tri) -> {
            map.computeIfAbsent(atr, k -> new TriNum.Mutable()).add(tri);
        });
    }
}
