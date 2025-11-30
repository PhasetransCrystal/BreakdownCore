package com.phasetranscrystal.breacore.api.material.registry;

import net.neoforged.neoforge.registries.DeferredHolder;

import com.phasetranscrystal.breacore.api.material.Material;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class MaterialEntry<M extends Material> extends RegistryEntry<Material, M> {

    public MaterialEntry(AbstractRegistrate<?> owner, DeferredHolder<Material, M> key) {
        super(owner, key);
    }
}
