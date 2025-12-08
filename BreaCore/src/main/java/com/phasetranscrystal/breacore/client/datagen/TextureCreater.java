package com.phasetranscrystal.breacore.client.datagen;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.item.TagPrefixItem;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.generators.RegistrateItemModelGenerator;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class TextureCreater {

    public static void init() {
        BreaCore.getModEventBus().addListener(EventPriority.HIGHEST, TextureCreater::onGatherData);
        loader = BreaCore.class.getClassLoader();
    }

    private static ClassLoader loader;
    private static DataGenerator dataGenerator;

    private static void onGatherData(GatherDataEvent.Client event) {
        dataGenerator = event.getGenerator();
    }

    public static void generageTagPrefixItemModel(DataGenContext<Item, TagPrefixItem> ctx, RegistrateItemModelGenerator prov) {
        var modId = ctx.getId().getNamespace();
        var item = ctx.getEntry();
        var mat = item.material;
        var tagPrefix = item.tagPrefix;
        var iconSet = mat.getMaterialIconSet();
        var iconType = tagPrefix.materialIconType();
        var mapping = new TextureMapping();

        var sourceIcon = iconType.getItemTexturePath(iconSet, true);
        var ras = loader.getResourceAsStream("assets/" + modId + "/textures/" + sourceIcon.getPath() + ".png");
        while (!iconSet.isRootIconset) {
            iconSet = iconSet.parentIconset;
            sourceIcon = iconType.getItemTexturePath(iconSet, true);
            ras = loader.getResourceAsStream("assets/" + modId + "/textures/" + sourceIcon.getPath() + ".png");
            if (ras != null)
                break;
        }
        if (ras == null) {
            defaultItemModel(ctx, prov);
            return;
        }
        BufferedImage source = null;
        try {
            source = ImageIO.read(ras);
            var width = source.getWidth();
            var height = source.getHeight();
            var color = mat.getMaterialARGB(0);
            var tintRed = (color >> 16) & 0xff;
            var tintGreen = (color >> 8) & 0xff;
            var tintBlue = color & 0xff;
            var sourceData = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            boolean genColor = mat.getMaterialARGB() != -1;
            for (var x = 0; x < width; x++) {
                for (var y = 0; y < height; y++) {
                    int pixel = source.getRGB(x, y);
                    int pixelAlpha = pixel >> 24 & 0xff;
                    if (!genColor || pixelAlpha > 0) {
                        int pixelRed = pixel >> 16 & 0xff;
                        int pixelGreen = pixel >> 8 & 0xff;
                        int pixelBlue = pixel & 0xff;

                        // 混合颜色
                        int blendedRed = (int) (pixelRed * tintRed) / 255;
                        int blendedGreen = (int) (pixelGreen * tintGreen) / 255;
                        int blendedBlue = (int) (pixelBlue * tintBlue) / 255;

                        int blendedPixel = (pixelAlpha << 24) | (blendedRed << 16) | (blendedGreen << 8) | blendedBlue;
                        sourceData.setRGB(x, y, blendedPixel);
                    } else {
                        sourceData.setRGB(x, y, pixel);
                    }
                }
            }
            var endrl = ResourceLocation.fromNamespaceAndPath(modId,
                    "item/" + iconSet.name + "/" +
                            tagPrefix.idPattern().formatted(mat.getName()));
            mapping.put(TextureSlot.LAYER0, endrl);
            var output = dataGenerator.getPackOutput().getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(modId)
                    .resolve("textures/" + endrl.getPath() + ".png");
            var of = output.toAbsolutePath().toFile();
            of.getParentFile().mkdirs();
            ImageIO.write(sourceData, "PNG", of);
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }
        var mrl = ModelTemplates.FLAT_ITEM.create(item, mapping, prov.modelOutput);
        prov.createWithExistingModel(ctx.getEntry(), mrl);
    }

    public static void defaultItemModel(DataGenContext<Item, TagPrefixItem> ctx, RegistrateItemModelGenerator prov) {
        prov.createFlatItemModel(ctx.getEntry(), ModelTemplates.FLAT_ITEM);
    }
}
