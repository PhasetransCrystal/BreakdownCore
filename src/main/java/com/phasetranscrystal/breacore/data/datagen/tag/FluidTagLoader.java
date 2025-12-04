package com.phasetranscrystal.breacore.data.datagen.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

public class FluidTagLoader {

    public static void init(RegistrateTagsProvider.Intrinsic<Fluid> provider) {}

    public static void create(RegistrateTagsProvider.Intrinsic<Fluid> provider, TagKey<Fluid> tag, Fluid... fluids) {
        var builder = provider.tag(tag);
        for (Fluid fluid : fluids) {
            builder.add(fluid);
        }
    }
}
