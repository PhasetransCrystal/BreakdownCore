package com.phasetranscrystal.breacore.data.materials.material;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class HigherDegreeMaterials {

    public static void register() {
        EnderEye = new MaterialBuilder(BreaUtil.byPath("ender_eye"))
                .gem(1)
                .color(0xb5e45a).secondaryColor(0x001430).iconSet(SHINY)
                .flags(GENERATE_PLATE, NO_SMASHING, NO_SMELTING, DECOMPOSITION_BY_CENTRIFUGING)
                .components(EnderPearl, 1, Blaze, 1)
                .buildAndRegister();

        Basalt = new MaterialBuilder(BreaUtil.byPath("basalt"))
                .dust(1)
                .color(0x5c5c5c).secondaryColor(0x1b2632).iconSet(ROUGH)
                .flags(NO_SMASHING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Granite = new MaterialBuilder(BreaUtil.byPath("granite"))
                .dust()
                .color(0xd69077).secondaryColor(0x71352c).iconSet(ROUGH)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Brick = new MaterialBuilder(BreaUtil.byPath("brick"))
                .dust()
                .color(0xc76245).secondaryColor(0x2d1610).iconSet(ROUGH)
                .flags(EXCLUDE_BLOCK_CRAFTING_RECIPES, NO_SMELTING, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Clay, 1)
                .buildAndRegister();

        Diorite = new MaterialBuilder(BreaUtil.byPath("diorite"))
                .dust()
                .color(0xe9e9e9).secondaryColor(0x7b7b7b)
                .iconSet(ROUGH)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Blackstone = new MaterialBuilder(BreaUtil.byPath("blackstone"))
                .dust()
                .color(0x090a0a).iconSet(ROUGH)
                .flags(NO_SMASHING)
                .buildAndRegister();
    }
}
