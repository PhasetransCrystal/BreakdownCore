package com.phasetranscrystal.breacore.mixin;

import com.phasetranscrystal.breacore.api.fluid.store.FluidStorage;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.registry.registrate.BreaFluidTypeExtensions;

import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class MixinHelpers {

    public static void addFluidTexture(Material material, FluidStorage.FluidEntry value) {
        if (value != null) {
            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(value.getFluid().get());
            if (extensions instanceof BreaFluidTypeExtensions gtExtensions && value.getBuilder() != null) {
                value.getBuilder().determineTextures(material, value.getKey());

                gtExtensions.setFlowingTexture(value.getBuilder().flowing());
                gtExtensions.setStillTexture(value.getBuilder().still());
            }
        }
    }
}
