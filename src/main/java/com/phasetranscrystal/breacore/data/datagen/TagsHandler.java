package com.phasetranscrystal.breacore.data.datagen;

import net.minecraft.world.item.Items;

import static com.phasetranscrystal.breacore.api.material.ItemMaterialData.registerMaterialEntry;
import static com.phasetranscrystal.breacore.api.material.MarkerMaterials.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;
import static com.phasetranscrystal.breacore.data.tagprefix.BreaTagPrefixes.*;

public class TagsHandler {

    public static void initExtraUnificationEntries() {
        registerMaterialEntry(Items.CLAY_BALL, ingot, Clay);

        registerMaterialEntry(Items.BLACK_DYE, dye, Color.Black);
        registerMaterialEntry(Items.RED_DYE, dye, Color.Red);
        registerMaterialEntry(Items.GREEN_DYE, dye, Color.Green);
        registerMaterialEntry(Items.BROWN_DYE, dye, Color.Brown);
        registerMaterialEntry(Items.BLUE_DYE, dye, Color.Blue);
        registerMaterialEntry(Items.PURPLE_DYE, dye, Color.Purple);
        registerMaterialEntry(Items.CYAN_DYE, dye, Color.Cyan);
        registerMaterialEntry(Items.LIGHT_GRAY_DYE, dye, Color.LightGray);
        registerMaterialEntry(Items.GRAY_DYE, dye, Color.Gray);
        registerMaterialEntry(Items.PINK_DYE, dye, Color.Pink);
        registerMaterialEntry(Items.LIME_DYE, dye, Color.Lime);
        registerMaterialEntry(Items.YELLOW_DYE, dye, Color.Yellow);
        registerMaterialEntry(Items.LIGHT_BLUE_DYE, dye, Color.LightBlue);
        registerMaterialEntry(Items.MAGENTA_DYE, dye, Color.Magenta);
        registerMaterialEntry(Items.ORANGE_DYE, dye, Color.Orange);
        registerMaterialEntry(Items.WHITE_DYE, dye, Color.White);
    }
}
