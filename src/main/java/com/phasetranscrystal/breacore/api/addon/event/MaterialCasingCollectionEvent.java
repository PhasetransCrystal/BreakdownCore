package com.phasetranscrystal.breacore.api.addon.event;

import net.minecraft.world.level.block.Block;

import com.google.common.collect.ImmutableMap;
import com.phasetranscrystal.breacore.api.material.Material;
import com.tterrag.registrate.util.entry.BlockEntry;

public class MaterialCasingCollectionEvent {

    private final ImmutableMap.Builder<Material, BlockEntry<Block>> builder;

    public MaterialCasingCollectionEvent(ImmutableMap.Builder<Material, BlockEntry<Block>> builder) {
        this.builder = builder;
    }

    public void add(Material material, BlockEntry<Block> casingBlock) {
        builder.put(material, casingBlock);
    }
}
