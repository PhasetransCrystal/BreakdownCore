package com.phasetranscrystal.breacore.data.materials.material;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.fluid.FluidRegisterBuilder;
import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class UnknownCompositionMaterials {

    public static void register() {
        Gunpowder = new MaterialBuilder(BreaUtil.byPath("gunpowder"))
                .dust(0)
                .color(0xa4a4a4).secondaryColor(0x767676).iconSet(ROUGH)
                .flags(FLAMMABLE, EXPLOSIVE, NO_SMELTING, NO_SMASHING)
                .buildAndRegister();

        Stone = new MaterialBuilder(BreaUtil.byPath("stone"))
                .dust(2)
                .color(0x8f8f8f).secondaryColor(0x898989).iconSet(ROUGH)
                .flags(MORTAR_GRINDABLE, GENERATE_GEAR, NO_SMASHING, NO_SMELTING)
                .buildAndRegister();

        Lava = new MaterialBuilder(BreaUtil.byPath("lava"))
                .fluid().color(0xFF4000).buildAndRegister();

        Netherite = new MaterialBuilder(BreaUtil.byPath("netherite"))
                .ingot().color(0x4b4042).secondaryColor(0x474447)
                .buildAndRegister();

        Glowstone = new MaterialBuilder(BreaUtil.byPath("glowstone"))
                .dust(1)
                .liquid(new FluidRegisterBuilder().temperature(500))
                .color(0xfcb34c).secondaryColor(0xce7533).iconSet(SHINY)
                .flags(NO_SMASHING, GENERATE_PLATE, EXCLUDE_PLATE_COMPRESSOR_RECIPE,
                        EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .buildAndRegister();

        NetherStar = new MaterialBuilder(BreaUtil.byPath("nether_star"))
                .gem(4)
                .color(0xfeffc6).secondaryColor(0x7fd7e2)
                .iconSet(NETHERSTAR)
                .flags(NO_SMASHING, NO_SMELTING, GENERATE_LENS)
                .buildAndRegister();

        Endstone = new MaterialBuilder(BreaUtil.byPath("endstone"))
                .dust(1)
                .color(0xf6fabd).secondaryColor(0xc5be8b).iconSet(ROUGH)
                .flags(NO_SMASHING)
                .buildAndRegister();

        Netherrack = new MaterialBuilder(BreaUtil.byPath("netherrack"))
                .dust(1)
                .color(0x7c4249).secondaryColor(0x400b0b).iconSet(ROUGH)
                .flags(NO_SMASHING, FLAMMABLE)
                .buildAndRegister();

        Milk = new MaterialBuilder(BreaUtil.byPath("milk"))
                .liquid(new FluidRegisterBuilder()
                        .temperature(295)
                        .customStill())
                .color(0xfffbf0).secondaryColor(0xf6eac8).iconSet(FINE)
                .buildAndRegister();

        Wood = new MaterialBuilder(BreaUtil.byPath("wood"))
                .wood()
                .color(0xc29f6d).secondaryColor(0x643200).iconSet(WOOD)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_LONG_ROD, FLAMMABLE, GENERATE_GEAR,
                        GENERATE_FRAME)
                .buildAndRegister();

        Paper = new MaterialBuilder(BreaUtil.byPath("paper"))
                .dust(0)
                .color(0xFAFAFA).secondaryColor(0x878787).iconSet(FINE)
                .flags(GENERATE_PLATE, FLAMMABLE, NO_SMELTING, NO_SMASHING,
                        MORTAR_GRINDABLE, EXCLUDE_PLATE_COMPRESSOR_RECIPE)
                .buildAndRegister();

        // These colors are much nicer looking than those in MC's EnumDyeColor
        DyeBlack = new MaterialBuilder(BreaUtil.byPath("black_dye"))
                .fluid().color(0x202020).buildAndRegister();

        DyeRed = new MaterialBuilder(BreaUtil.byPath("red_dye"))
                .fluid().color(0xFF0000).buildAndRegister();

        DyeGreen = new MaterialBuilder(BreaUtil.byPath("green_dye"))
                .fluid().color(0x00FF00).buildAndRegister();

        DyeBrown = new MaterialBuilder(BreaUtil.byPath("brown_dye"))
                .fluid().color(0x604000).buildAndRegister();

        DyeBlue = new MaterialBuilder(BreaUtil.byPath("blue_dye"))
                .fluid().color(0x0020FF).buildAndRegister();

        DyePurple = new MaterialBuilder(BreaUtil.byPath("purple_dye"))
                .fluid().color(0x800080).buildAndRegister();

        DyeCyan = new MaterialBuilder(BreaUtil.byPath("cyan_dye"))
                .fluid().color(0x00FFFF).buildAndRegister();

        DyeLightGray = new MaterialBuilder(BreaUtil.byPath("light_gray_dye"))
                .fluid().color(0xC0C0C0).buildAndRegister();

        DyeGray = new MaterialBuilder(BreaUtil.byPath("gray_dye"))
                .fluid().color(0x808080).buildAndRegister();

        DyePink = new MaterialBuilder(BreaUtil.byPath("pink_dye"))
                .fluid().color(0xFFC0C0).buildAndRegister();

        DyeLime = new MaterialBuilder(BreaUtil.byPath("lime_dye"))
                .fluid().color(0x80FF80).buildAndRegister();

        DyeYellow = new MaterialBuilder(BreaUtil.byPath("yellow_dye"))
                .fluid().color(0xFFFF00).buildAndRegister();

        DyeLightBlue = new MaterialBuilder(BreaUtil.byPath("light_blue_dye"))
                .fluid().color(0x6080FF).buildAndRegister();

        DyeMagenta = new MaterialBuilder(BreaUtil.byPath("magenta_dye"))
                .fluid().color(0xFF00FF).buildAndRegister();

        DyeOrange = new MaterialBuilder(BreaUtil.byPath("orange_dye"))
                .fluid().color(0xFF8000).buildAndRegister();

        DyeWhite = new MaterialBuilder(BreaUtil.byPath("white_dye"))
                .fluid().color(0xFFFFFF).buildAndRegister();

        TreatedWood = new MaterialBuilder(BreaUtil.byPath("treated_wood"))
                .wood()
                .color(0x644218).secondaryColor(0x4e0b00).iconSet(WOOD)
                .flags(GENERATE_PLATE, FLAMMABLE, GENERATE_ROD, GENERATE_FRAME)
                .buildAndRegister();

        Sculk = new MaterialBuilder(BreaUtil.byPath("sculk"))
                .dust(1)
                .color(0x015a5c).secondaryColor(0x001616).iconSet(ROUGH)
                .buildAndRegister();

        Wax = new MaterialBuilder(BreaUtil.byPath("wax"))
                .ingot().fluid()
                .color(0xfabf29)
                .flags(NO_SMELTING)
                .buildAndRegister();
    }
}
