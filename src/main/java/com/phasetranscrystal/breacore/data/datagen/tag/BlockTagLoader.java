package com.phasetranscrystal.breacore.data.datagen.tag;

import com.phasetranscrystal.breacore.data.tags.CustomTags;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

public class BlockTagLoader {

    public static void init(RegistrateTagsProvider.Intrinsic<Block> provider) {
        provider.tag(CustomTags.ENDSTONE_ORE_REPLACEABLES)
                .addTag(Tags.Blocks.END_STONES);

        provider.tag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .addTag(CustomTags.NEEDS_DURANIUM_TOOL)
                .addTag(CustomTags.NEEDS_NEUTRONIUM_TOOL);
        provider.tag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL)
                .addTag(CustomTags.NEEDS_DURANIUM_TOOL)
                .addTag(CustomTags.NEEDS_NEUTRONIUM_TOOL);
        provider.tag(CustomTags.INCORRECT_FOR_DURANIUM_TOOL)
                .addTag(CustomTags.NEEDS_NEUTRONIUM_TOOL);

        // this is awful. I don't care, though.

        // provider.tag(BlockTags.MINEABLE_WITH_AXE)
        // .add(TagEntry.element(GTMachines.WOODEN_DRUM.getId()))
        // .add(TagEntry.element(GTMachines.WOODEN_CRATE.getId()));

        // always add the wrench/pickaxe tag as a valid tag to mineable/wrench etc.
        provider.tag(CustomTags.MINEABLE_WITH_WRENCH)
                .addTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH);
        provider.tag(CustomTags.MINEABLE_WITH_WIRE_CUTTER)
                .addTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER);
    }
}
