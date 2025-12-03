package com.phasetranscrystal.breacore.common.block;

import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.tag.TagPrefix;
import com.phasetranscrystal.breacore.data.materials.BreaMaterials;
import com.phasetranscrystal.breacore.data.tagprefix.BreaTagPrefixes;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum StoneTypes implements StringRepresentable {

    STONE("stone", MapColor.STONE, true, () -> Blocks.STONE::defaultBlockState, BreaMaterials.Stone, false),
    GRANITE("granite", MapColor.DIRT, true, () -> Blocks.GRANITE::defaultBlockState, BreaMaterials.Granite, false),
    DIORITE("diorite", MapColor.QUARTZ, true, () -> Blocks.DIORITE::defaultBlockState, BreaMaterials.Diorite, false),
    ANDESITE("andesite", MapColor.STONE, true, () -> Blocks.ANDESITE::defaultBlockState, BreaMaterials.Andesite, false),
    DEEPSLATE("deepslate", MapColor.DEEPSLATE, true, () -> Blocks.DEEPSLATE::defaultBlockState, BreaMaterials.Deepslate, false),
    BASALT("basalt", MapColor.TERRACOTTA_BLACK, true, () -> Blocks.BASALT::defaultBlockState, BreaMaterials.Basalt, false),
    TUFF("tuff", MapColor.TERRACOTTA_GRAY, true, () -> Blocks.TUFF::defaultBlockState, BreaMaterials.Tuff, false),
    BLACKSTONE("blackstone", MapColor.COLOR_BLACK, true, () -> Blocks.BLACKSTONE::defaultBlockState, BreaMaterials.Blackstone, false),
    ;

    private final String name;
    public final MapColor mapColor;
    @Getter
    public final boolean natural;
    @Getter
    public final Supplier<Supplier<BlockState>> state;
    @Getter
    public final Material material;

    public final boolean generateBlocks;

    StoneTypes(@NotNull String name, @NotNull MapColor mapColor, boolean natural, Supplier<Supplier<BlockState>> state,
               Material material) {
        this(name, mapColor, natural, state, material, true);
    }

    StoneTypes(@NotNull String name, @NotNull MapColor mapColor, boolean natural, Supplier<Supplier<BlockState>> state,
               Material material, boolean generateBlocks) {
        this.name = name;
        this.mapColor = mapColor;
        this.natural = natural;
        this.state = state;
        this.material = material;
        this.generateBlocks = generateBlocks;
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return this.name;
    }

    public TagPrefix getTagPrefix() {
        return BreaTagPrefixes.block;
    }

    public static void init() {}
}
