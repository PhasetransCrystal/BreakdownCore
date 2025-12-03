package com.phasetranscrystal.breacore.utils.memoization;

import net.minecraft.world.level.block.Block;

import com.phasetranscrystal.breacore.api.material.stack.MaterialEntry;

import java.util.function.Supplier;

/**
 * A variant of the memoized supplier that stores a block explicitly.
 * Use this to save blocks to
 * {@link com.phasetranscrystal.breacore.api.material.ItemMaterialData#registerMaterialEntry(Supplier, MaterialEntry)}}
 */
public class MemoizedBlockSupplier<T extends Block> extends MemoizedSupplier<T> {

    protected MemoizedBlockSupplier(Supplier<T> delegate) {
        super(delegate);
    }
}
