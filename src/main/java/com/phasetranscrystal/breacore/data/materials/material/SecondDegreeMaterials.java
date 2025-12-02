package com.phasetranscrystal.breacore.data.materials.material;

import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class SecondDegreeMaterials {

    public static void register() {
        Glass = new MaterialBuilder(BreaUtil.byPath("glass"))
                .gem(0)
                .color(0xffffff).iconSet(GLASS)
                .flags(GENERATE_LENS, NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_RECIPES, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 1)
                .buildAndRegister();

        Amethyst = new MaterialBuilder(BreaUtil.byPath("amethyst"))
                .gem(3).ore()
                .color(0xcfa0f3).secondaryColor(0x734fbc).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, GENERATE_LENS)
                .components(SiliconDioxide, 4, Iron, 1)
                .buildAndRegister();

        EchoShard = new MaterialBuilder(BreaUtil.byPath("echo_shard"))
                .gem(3)
                .color(0x002b2d).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, GENERATE_ROD)
                .components(SiliconDioxide, 3, Sculk, 2)
                .buildAndRegister();

        Lapis = new MaterialBuilder(BreaUtil.byPath("lapis"))
                .gem(1).ore(6, 4)
                .color(0x85a9ff).secondaryColor(0x2a7fff).iconSet(LAPIS)
                .flags(NO_SMASHING, NO_SMELTING, CRYSTALLIZABLE, NO_WORKING, DECOMPOSITION_BY_ELECTROLYZING,
                        EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        GENERATE_PLATE, GENERATE_ROD)
                .buildAndRegister();

        Blaze = new MaterialBuilder(BreaUtil.byPath("blaze"))
                .dust(1)
                .color(0xfff94d, false).secondaryColor(0xff330c).iconSet(FINE)
                .flags(NO_SMELTING, MORTAR_GRINDABLE, DECOMPOSITION_BY_CENTRIFUGING) // todo burning flag
                .buildAndRegister();

        Deepslate = new MaterialBuilder(BreaUtil.byPath("deepslate"))
                .dust()
                .color(0x797979).secondaryColor(0x2f2f37).iconSet(ROUGH)
                .flags(NO_SMASHING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Concrete = new MaterialBuilder(BreaUtil.byPath("concrete"))
                .dust()
                .color(0xfaf3e8).secondaryColor(0xbbbaba).iconSet(ROUGH)
                .flags(NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .components(Stone, 1)
                .buildAndRegister();

        Andesite = new MaterialBuilder(BreaUtil.byPath("andesite"))
                .dust()
                .color(0xa8aa9a).iconSet(ROUGH)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Flint = new MaterialBuilder(BreaUtil.byPath("flint"))
                .gem(1)
                .color(0xc7c7c7).secondaryColor(0x212121).iconSet(FLINT)
                .flags(NO_SMASHING, MORTAR_GRINDABLE, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 1)
                .buildAndRegister();

        Clay = new MaterialBuilder(BreaUtil.byPath("clay"))
                .dust(1)
                .color(0xbec9e8).secondaryColor(0x373944).iconSet(ROUGH)
                .flags(MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .components(Sodium, 2, Lithium, 1, Aluminium, 2, Silicon, 2, Water, 6)
                .buildAndRegister();

        Redstone = new MaterialBuilder(BreaUtil.byPath("redstone"))
                .dust().ore(5, 1, true)
                .color(0xff0000).secondaryColor(0x340605).iconSet(ROUGH)
                .flags(GENERATE_PLATE, NO_SMASHING, NO_SMELTING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        EXCLUDE_PLATE_COMPRESSOR_RECIPE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
    }
}
