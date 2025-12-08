package com.phasetranscrystal.brealib.mui.texture;

import icyllis.modernui.graphics.Image;

import net.minecraft.resources.ResourceLocation;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuiTexture {

    private static final Map<ResourceLocation, GuiTexture> cacheTextures = new HashMap<>();

    public static Optional<Image> getTexture(ResourceLocation location) {
        if (cacheTextures.containsKey(location)) {
            return cacheTextures.get(location).getTexture();
        }
        return Optional.empty();
    }

    @Getter
    private ResourceLocation location;
    private Image texture;

    public Optional<Image> getTexture() {
        if (texture == null || texture.isClosed()) {
            texture = Image.create(location.getNamespace(), location.getPath());
        }
        return Optional.ofNullable(texture);
    }

    public GuiTexture(ResourceLocation location) {
        this(location, false);
    }

    public GuiTexture(ResourceLocation location, boolean loadNow) {
        this.location = location;
        cacheTextures.put(location, this);
        if (loadNow) {
            getTexture();
        }
    }

    public GuiTexture copy() {
        return new GuiTexture(location);
    }
}
