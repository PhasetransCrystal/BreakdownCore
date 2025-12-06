package com.phasetranscrystal.breacore.api.machine.mulityblock;

import com.phasetranscrystal.breacore.utils.memoization.CacheMemoizer;

import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/3/4
 * @implNote MultiblockAbility
 *           Fine, It's not really needed。It used to specify which blocks are available.
 *           Only registered blocks can be used as part of gtceu's multiblock.
 */
public class PartAbility {

    /**
     * tier -> available blocks
     */
    private final Int2ObjectMap<Set<Block>> registry = new Int2ObjectOpenHashMap<>();

    private final Supplier<Collection<Block>> allBlocks = CacheMemoizer
            .memoize(() -> registry.values().stream().flatMap(Collection::stream).toList());

    @Getter
    private final String name;

    public PartAbility(String name) {
        this.name = name;
    }

    public void register(int tier, Block block) {
        registry.computeIfAbsent(tier, T -> new HashSet<>()).add(block);
    }

    public Collection<Block> getAllBlocks() {
        return allBlocks.get();
    }

    public boolean isApplicable(Block block) {
        return getAllBlocks().contains(block);
    }

    public Collection<Block> getBlocks(int... tiers) {
        return registry.int2ObjectEntrySet().stream()
                .filter(entry -> ArrayUtils.contains(tiers, entry.getIntKey()))
                .flatMap(entry -> entry.getValue().stream())
                .toList();
    }

    /**
     * [from, to]
     */
    public Collection<Block> getBlockRange(int from, int to) {
        return registry.int2ObjectEntrySet().stream()
                .filter(entry -> entry.getIntKey() <= to && entry.getIntKey() >= from)
                .flatMap(entry -> entry.getValue().stream())
                .toList();
    }
}
