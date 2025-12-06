package com.phasetranscrystal.breacore.data.materials.material;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.fluid.FluidRegisterBuilder;
import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class SecondDegreeMaterials {

    public static void register() {
        Glass = new MaterialBuilder(BreaUtil.byPath("glass"))
                .gem(0)
                .liquid(new FluidRegisterBuilder()
                        .temperature(1200)
                        .customStill())
                .color(0xffffff).iconSet(DULL)
                .flags(GENERATE_LENS, NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_RECIPES, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 1)
                .buildAndRegister();

        Amethyst = new MaterialBuilder(BreaUtil.byPath("amethyst"))
                .gem(3).ore()
                .color(0xcfa0f3).secondaryColor(0x734fbc).iconSet(DULL)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, GENERATE_LENS)
                .components(SiliconDioxide, 4, Iron, 1)
                .buildAndRegister();

        EchoShard = new MaterialBuilder(BreaUtil.byPath("echo_shard"))
                .gem(3)
                .color(0x002b2d).iconSet(DULL)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, GENERATE_ROD)
                .components(SiliconDioxide, 3, Sculk, 2)
                .buildAndRegister();

        Lapis = new MaterialBuilder(BreaUtil.byPath("lapis"))
                .gem(1).ore(6, 4)
                .color(0x85a9ff).secondaryColor(0x2a7fff).iconSet(DULL)
                .flags(NO_SMASHING, NO_SMELTING, CRYSTALLIZABLE, NO_WORKING, DECOMPOSITION_BY_ELECTROLYZING,
                        EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        GENERATE_PLATE, GENERATE_ROD)
                .buildAndRegister();

        Blaze = new MaterialBuilder(BreaUtil.byPath("blaze"))
                .dust(1)
                .liquid(new FluidRegisterBuilder()
                        .temperature(4000)
                        .customStill())
                .color(0xfff94d, false).secondaryColor(0xff330c)
                .iconSet(DULL)
                .flags(NO_SMELTING, MORTAR_GRINDABLE, DECOMPOSITION_BY_CENTRIFUGING) // todo burning flag
                .buildAndRegister();

        Deepslate = new MaterialBuilder(BreaUtil.byPath("deepslate"))
                .dust()
                .color(0x797979).secondaryColor(0x2f2f37).iconSet(DULL)
                .flags(NO_SMASHING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Concrete = new MaterialBuilder(BreaUtil.byPath("concrete"))
                .dust()
                .liquid(new FluidRegisterBuilder().temperature(286))
                .color(0xfaf3e8).secondaryColor(0xbbbaba).iconSet(DULL)
                .flags(NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .components(Stone, 1)
                .buildAndRegister();

        Andesite = new MaterialBuilder(BreaUtil.byPath("andesite"))
                .dust()
                .color(0xa8aa9a).iconSet(DULL)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Flint = new MaterialBuilder(BreaUtil.byPath("flint"))
                .gem(1)
                .color(0xc7c7c7).secondaryColor(0x212121).iconSet(DULL)
                .flags(NO_SMASHING, MORTAR_GRINDABLE, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 1)
                .buildAndRegister();

        Air = new MaterialBuilder(BreaUtil.byPath("air"))
                .gas(new FluidRegisterBuilder().customStill())
                .color(0xA9D0F5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 78, Oxygen, 21, Argon, 9)
                .buildAndRegister();

        LiquidAir = new MaterialBuilder(BreaUtil.byPath("liquid_air"))
                .liquid(new FluidRegisterBuilder().temperature(97))
                .color(0xA9D0F5)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NetherAir = new MaterialBuilder(BreaUtil.byPath("nether_air"))
                .gas()
                .color(0x4C3434)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LiquidNetherAir = new MaterialBuilder(BreaUtil.byPath("liquid_nether_air"))
                .liquid(new FluidRegisterBuilder().temperature(58))
                .color(0x4C3434)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnderAir = new MaterialBuilder(BreaUtil.byPath("ender_air"))
                .gas()
                .color(0x283454)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LiquidEnderAir = new MaterialBuilder(BreaUtil.byPath("liquid_ender_air"))
                .liquid(new FluidRegisterBuilder().temperature(36))
                .color(0x283454)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Clay = new MaterialBuilder(BreaUtil.byPath("clay"))
                .dust(1)
                .color(0xbec9e8).secondaryColor(0x373944).iconSet(DULL)
                .flags(MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .components(Sodium, 2, Lithium, 1, Aluminium, 2, Silicon, 2, Water, 6)
                .buildAndRegister();

        Redstone = new MaterialBuilder(BreaUtil.byPath("redstone"))
                .dust().ore(5, 1, true)
                .liquid(new FluidRegisterBuilder().temperature(500))
                .color(0xff0000).secondaryColor(0x340605).iconSet(DULL)
                .flags(GENERATE_PLATE, NO_SMASHING, NO_SMELTING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        EXCLUDE_PLATE_COMPRESSOR_RECIPE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
    }
}
