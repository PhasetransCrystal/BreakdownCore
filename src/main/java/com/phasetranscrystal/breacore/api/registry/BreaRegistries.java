package com.phasetranscrystal.breacore.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class BreaRegistries {

    public static <T> ResourceKey<Registry<T>> makeRegistryKey(ResourceLocation registryId) {
        return ResourceKey.createRegistryKey(registryId);
    }
}
