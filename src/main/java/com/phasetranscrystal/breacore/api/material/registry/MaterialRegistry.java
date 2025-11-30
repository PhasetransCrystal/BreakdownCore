package com.phasetranscrystal.breacore.api.material.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.registry.BreaRegistry;

public class MaterialRegistry<T extends Material> extends BreaRegistry<T> implements IMaterialRegistry {

    public MaterialRegistry(ResourceKey<Registry<T>> registryKey) {
        super(registryKey);
    }
}
