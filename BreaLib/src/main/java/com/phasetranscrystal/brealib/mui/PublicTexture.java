package com.phasetranscrystal.brealib.mui;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.mui.texture.GuiTexture;

import net.minecraft.resources.ResourceLocation;

public class PublicTexture {

    public static final GuiTexture ROOT_LEFT_DEC = new GuiTexture(ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "ui_public/left_dec.png"), true);
    public static final GuiTexture ROOT_NAME_DEC = new GuiTexture(ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "ui_public/name_dec.png"), true);
    public static final GuiTexture ROOT_CLOSE_BUTTON = new GuiTexture(ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "ui_public/close.png"), true);
    public static final GuiTexture ROOT_INSIDE_DEC1 = new GuiTexture(ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "ui_public/dec1.png"), true);

    public static final GuiTexture ICON_BACKPACK = new GuiTexture(ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "ui_public/icon/backpack.png"), true);
}
