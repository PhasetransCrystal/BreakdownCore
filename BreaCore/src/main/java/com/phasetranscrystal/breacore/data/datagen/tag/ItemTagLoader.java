package com.phasetranscrystal.breacore.data.datagen.tag;

import com.phasetranscrystal.breacore.api.material.ChemicalHelper;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.tag.TagPrefix;
import com.phasetranscrystal.breacore.data.items.MaterialItems;
import com.phasetranscrystal.breacore.data.tagprefix.BreaTagPrefixes;
import com.phasetranscrystal.breacore.data.tags.CustomTags;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.neoforged.neoforge.common.Tags;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import java.util.Objects;

import static com.phasetranscrystal.breacore.api.material.MarkerMaterials.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;
import static com.phasetranscrystal.breacore.data.tagprefix.BreaTagPrefixes.*;

public class ItemTagLoader {

    public static void init(RegistrateTagsProvider.Intrinsic<Item> provider) {
        addTag(provider, lens, Color.White)
                .add(MaterialItems.MATERIAL_ITEMS.get(lens, Glass).get())
                .add(MaterialItems.MATERIAL_ITEMS.get(lens, NetherStar).get());
        addTag(provider, lens, Color.LightBlue).add(MaterialItems.MATERIAL_ITEMS.get(lens, Diamond).get());
        addTag(provider, lens, Color.Green).add(MaterialItems.MATERIAL_ITEMS.get(lens, Emerald).get());
        addTag(provider, lens, Color.Purple).add(MaterialItems.MATERIAL_ITEMS.get(lens, Amethyst).get());

        provider.tag(CustomTags.PISTONS)
                .add(Items.PISTON)
                .add(Items.STICKY_PISTON);

        // add treated wood stick to vanilla sticks tag
        // noinspection DataFlowIssue ChemicalHelper#getTag can't return null with treated wood rod
        provider.tag(Tags.Items.RODS_WOODEN)
                .add(MaterialItems.MATERIAL_ITEMS.get(BreaTagPrefixes.rod, TreatedWood).get());

        // add treated and untreated wood plates to vanilla planks tag
        provider.tag(ItemTags.PLANKS)
                .add(MaterialItems.MATERIAL_ITEMS.get(plate, TreatedWood).get())
                .add(MaterialItems.MATERIAL_ITEMS.get(plate, Wood).get());
    }

    private static TagAppender<Item, Item> addTag(RegistrateTagsProvider.Intrinsic<Item> provider,
                                                  TagPrefix prefix, Material material) {
        return provider.tag(Objects.requireNonNull(ChemicalHelper.getTag(prefix, material),
                "%s/%s doesn't have any tags!".formatted(prefix, material)));
    }

    private static void create(RegistrateTagsProvider.Intrinsic<Item> provider, TagPrefix prefix, Material material,
                               Item... rls) {
        create(provider, ChemicalHelper.getTag(prefix, material), rls);
    }

    @SafeVarargs
    public static void create(RegistrateTagsProvider.Intrinsic<Item> provider, TagKey<Item> tagKey, TagKey<Item>... rls) {
        var builder = provider.tag(tagKey);
        for (TagKey<Item> tag : rls) {
            builder.addTag(tag);
        }
    }

    public static void create(RegistrateTagsProvider.Intrinsic<Item> provider, TagKey<Item> tagKey, Item... rls) {
        var builder = provider.tag(tagKey);
        for (Item item : rls) {
            builder.add(item);
        }
    }

    private static ResourceLocation rl(String name) {
        return ResourceLocation.parse(name);
    }
}
