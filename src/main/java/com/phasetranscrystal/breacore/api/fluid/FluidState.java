package com.phasetranscrystal.breacore.api.fluid;

import com.phasetranscrystal.breacore.data.tags.CustomTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum FluidState {

    LIQUID("breacore.fluid.state_liquid", CustomTags.LIQUID_FLUIDS),
    GAS("breacore.fluid.state_gas", Tags.Fluids.GASEOUS),
    PLASMA("breacore.fluid.state_plasma", CustomTags.PLASMA_FLUIDS),
    ;

    @Getter
    private final String translationKey;
    @Getter
    private final TagKey<Fluid> tagKey;

    FluidState(@NotNull String translationKey, @NotNull TagKey<Fluid> tagKey) {
        this.translationKey = translationKey;
        this.tagKey = tagKey;
    }
}
