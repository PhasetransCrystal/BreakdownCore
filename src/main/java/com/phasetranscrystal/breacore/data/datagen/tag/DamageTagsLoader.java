package com.phasetranscrystal.breacore.data.datagen.tag;

import com.phasetranscrystal.breacore.BreaCore;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class DamageTagsLoader extends TagsProvider<DamageType> {

    protected DamageTagsLoader(PackOutput output, ResourceKey<? extends Registry<DamageType>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, registryKey, lookupProvider, BreaCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // DamageTypeData.allInNamespace(BreaCore.MOD_ID).forEach(damageTypeData ->
        // damageTypeData.tags.forEach(damageTypeTagKey -> tag(damageTypeTagKey).add(damageTypeData.key)));
    }
}
