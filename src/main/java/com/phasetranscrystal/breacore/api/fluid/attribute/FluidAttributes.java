package com.phasetranscrystal.breacore.api.fluid.attribute;

import net.minecraft.network.chat.Component;

import com.phasetranscrystal.brealib.utils.BreaUtil;

public final class FluidAttributes {

    /**
     * Attribute for acidic fluids.
     */
    public static final FluidAttribute ACID = new FluidAttribute(BreaUtil.byPath("acid"),
            list -> list.accept(Component.translatable("breacore.fluid.type_acid.tooltip")),
            list -> list.accept(Component.translatable("breacore.fluid_pipe.acid_proof")));

    private FluidAttributes() {}
}
