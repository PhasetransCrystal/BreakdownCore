package com.phasetranscrystal.breacore.common.quench.perk;

import com.mojang.serialization.Codec;

import java.util.Map;

public record ExtraPerkWeightComponent(int value) implements IPerkElemProvider {

    public static final Codec<ExtraPerkWeightComponent> CODEC = Codec.INT.xmap(ExtraPerkWeightComponent::new, ExtraPerkWeightComponent::value);

    @Override
    public int extraPerkWeight() {
        return value;
    }

    @Override
    public Map<Perk, Double> getPerkAndStrength() {
        return Map.of();
    }
}
