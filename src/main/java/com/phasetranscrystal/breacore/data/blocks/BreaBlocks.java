package com.phasetranscrystal.breacore.data.blocks;

import net.minecraft.world.level.block.Block;

import com.google.common.collect.Table;
import com.phasetranscrystal.breacore.api.material.ItemMaterialData;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.stack.MaterialEntry;
import com.phasetranscrystal.breacore.api.tag.TagPrefix;
import com.phasetranscrystal.breacore.common.block.StoneBlockType;
import com.phasetranscrystal.breacore.common.block.StoneTypes;
import com.phasetranscrystal.breacore.data.items.MaterialItems;
import com.phasetranscrystal.breacore.utils.memoization.CacheMemoizer;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BreaBlocks {

    public static void init() {}

    public static Table<StoneBlockType, StoneTypes, BlockEntry<Block>> STONE_BLOCKS;

    public static <P, T extends Block,
            S2 extends BlockBuilder<T, P>> NonNullFunction<S2, S2> unificationBlock(@NotNull TagPrefix tagPrefix,
                                                                                    @NotNull Material mat) {
        return builder -> {
            builder.onRegister(block -> {
                Supplier<Block> blockSupplier = CacheMemoizer.memoizeBlockSupplier(() -> block);
                MaterialEntry entry = new MaterialEntry(tagPrefix, mat);
                MaterialItems.toUnify.put(entry, blockSupplier);
                ItemMaterialData.registerMaterialEntry(blockSupplier, entry);
            });
            return builder;
        };
    }
}
