package com.phasetranscrystal.breacore.utils;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.net.URL;

import javax.annotation.Nonnull;

public class ResourceHelper {

    public static boolean isResourceExistRaw(ResourceLocation rs) {
        URL url = ResourceHelper.class.getResource(String.format("/assets/%s/%s", rs.getNamespace(), rs.getPath()));
        return url != null;
    }

    public static boolean isResourceExist(ResourceLocation rs) {
        if (BreaUtil.isClientSide()) {
            return Minecraft.getInstance().getResourceManager().getResource(rs).isPresent();
        } else {
            return false;
        }
    }

    public static boolean isTextureExist(@Nonnull ResourceLocation location) {
        var textureLocation = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "textures/%s.png".formatted(location.getPath()));
        return isResourceExist(textureLocation) || isResourceExistRaw(textureLocation);
    }

    public static boolean isModelExist(@Nonnull ResourceLocation location) {
        var modelLocation = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "models/%s.json".formatted(location.getPath()));
        return isResourceExist(modelLocation) || isResourceExistRaw(modelLocation);
    }
}
