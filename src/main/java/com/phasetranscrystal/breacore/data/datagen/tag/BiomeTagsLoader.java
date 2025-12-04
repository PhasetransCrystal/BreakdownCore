package com.phasetranscrystal.breacore.data.datagen.tag;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.data.tags.CustomTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class BiomeTagsLoader extends BiomeTagsProvider {

    public BiomeTagsLoader(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BreaCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CustomTags.HAS_RUBBER_TREE).addTag(Tags.Biomes.IS_SWAMP).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_JUNGLE);
    }
}
