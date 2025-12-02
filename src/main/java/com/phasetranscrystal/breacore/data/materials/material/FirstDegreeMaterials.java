package com.phasetranscrystal.breacore.data.materials.material;

import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class FirstDegreeMaterials {

    public static void register() {
        Bone = new MaterialBuilder(BreaUtil.byPath("bone"))
                .dust(1)
                .color(0xfcfbed).secondaryColor(0xa0a38b)
                .flags(MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, DISABLE_DECOMPOSITION)
                .components(Calcium, 3)
                .buildAndRegister();

        Calcite = new MaterialBuilder(BreaUtil.byPath("calcite"))
                .dust(1).ore()
                .color(0xfffef8).secondaryColor(0xbbaf62)
                .components(Calcium, 1, Carbon, 1, Oxygen, 3)
                .buildAndRegister();

        Charcoal = new MaterialBuilder(BreaUtil.byPath("charcoal"))
                .gem(1, 1600) // default charcoal burn time in vanilla
                .color(0x7d6f58).secondaryColor(0x13110d).iconSet(FINE)
                .flags(FLAMMABLE, NO_SMELTING, NO_SMASHING, MORTAR_GRINDABLE)
                .components(Carbon, 1)
                .buildAndRegister();

        Water = new MaterialBuilder(BreaUtil.byPath("water"))
                .color(0x0000FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Oxygen, 1)
                .buildAndRegister();

        Coal = new MaterialBuilder(BreaUtil.byPath("coal"))
                .gem(1, 1600).ore(2, 1) // default coal burn time in vanilla
                .color(0x393e41).secondaryColor(0x101015).iconSet(LIGNITE)
                .flags(FLAMMABLE, NO_SMELTING, NO_SMASHING, MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        DISABLE_DECOMPOSITION)
                .components(Carbon, 1)
                .buildAndRegister();

        Diamond = new MaterialBuilder(BreaUtil.byPath("diamond"))
                .gem(3).ore()
                .color(0xC8FFFF).iconSet(DIAMOND)
                .flags(GENERATE_BOLT_SCREW, GENERATE_LENS, GENERATE_GEAR, NO_SMASHING, NO_SMELTING,
                        HIGH_SIFTER_OUTPUT, DISABLE_DECOMPOSITION, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        GENERATE_LONG_ROD)
                .components(Carbon, 1)
                .buildAndRegister();

        Emerald = new MaterialBuilder(BreaUtil.byPath("emerald"))
                .gem().ore(2, 1)
                .color(0x17ff6c).secondaryColor(0x003f00).iconSet(EMERALD)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT,
                        EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, GENERATE_LENS)
                .components(Beryllium, 3, Aluminium, 2, Silicon, 6, Oxygen, 18)
                .buildAndRegister();

        Ice = new MaterialBuilder(BreaUtil.byPath("ice"))
                .dust(0)
                .color(0xeef6ff, false).secondaryColor(0x6389c9).iconSet(SHINY)
                .flags(NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Oxygen, 1)
                .buildAndRegister();

        Obsidian = new MaterialBuilder(BreaUtil.byPath("obsidian"))
                .dust(3)
                .color(0x3b2754).secondaryColor(0x000001).iconSet(SHINY)
                .flags(NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_RECIPES, GENERATE_PLATE, GENERATE_DENSE)
                .components(Magnesium, 1, Iron, 1, Silicon, 2, Oxygen, 4)
                .buildAndRegister();

        NetherQuartz = new MaterialBuilder(BreaUtil.byPath("nether_quartz"))
                .gem(1).ore(2, 1)
                .color(0xf8efe3).secondaryColor(0xe6c1bb).iconSet(QUARTZ)
                .flags(GENERATE_PLATE, NO_SMELTING, CRYSTALLIZABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        DISABLE_DECOMPOSITION)
                .components(Silicon, 1, Oxygen, 2)
                .buildAndRegister();

        CertusQuartz = new MaterialBuilder(BreaUtil.byPath("certus_quartz"))
                .gem(1).ore(2, 1)
                .color(0xc2d6ff).secondaryColor(0x86bacf).iconSet(CERTUS)
                .flags(GENERATE_PLATE, NO_SMELTING, CRYSTALLIZABLE, DISABLE_DECOMPOSITION)
                .components(Silicon, 1, Oxygen, 2)
                .buildAndRegister();

        SiliconDioxide = new MaterialBuilder(BreaUtil.byPath("silicon_dioxide"))
                .dust(1)
                .color(0xf2f2f2).secondaryColor(0xb2c4c7).iconSet(QUARTZ)
                .flags(NO_SMASHING, NO_SMELTING)
                .components(Silicon, 1, Oxygen, 2)
                .buildAndRegister();

        EnderPearl = new MaterialBuilder(BreaUtil.byPath("ender_pearl"))
                .gem(1)
                .color(0x8cf4e2).secondaryColor(0x032620).iconSet(SHINY)
                .flags(NO_SMASHING, NO_SMELTING, GENERATE_PLATE)
                .components(Beryllium, 1, Potassium, 4, Nitrogen, 5)
                .buildAndRegister();
    }
}
