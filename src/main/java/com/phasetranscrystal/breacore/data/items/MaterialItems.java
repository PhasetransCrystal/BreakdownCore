package com.phasetranscrystal.breacore.data.items;

import net.minecraft.world.level.ItemLike;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.phasetranscrystal.breacore.api.BreaAPI;
import com.phasetranscrystal.breacore.api.item.TagPrefixItem;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.stack.MaterialEntry;
import com.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;
import com.phasetranscrystal.breacore.api.tag.TagPrefix;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static com.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs.*;

public class MaterialItems {

    // Reference Table Builders
    static ImmutableTable.Builder<TagPrefix, Material, ItemEntry<TagPrefixItem>> MATERIAL_ITEMS_BUILDER = ImmutableTable
            .builder();

    // Reference Maps
    public static final Map<MaterialEntry, Supplier<? extends ItemLike>> toUnify = new HashMap<>();
    public static final Map<TagPrefix, TagPrefix> purifyMap = new HashMap<>();

    static {

    }

    // Reference Tables
    public static Table<TagPrefix, Material, ItemEntry<TagPrefixItem>> MATERIAL_ITEMS;

    public static void init() {
        REGISTRATE.creativeModeTab(() -> MATERIAL_ITEM);
        for (var tagPrefix : TagPrefix.values()) {
            if (tagPrefix.doGenerateItem()) {
                for (Material material : BreaAPI.materialManager) {
                    BreaRegistrate registrate = BreaRegistrate.createIgnoringListenerErrors(material.getModid());
                    if (tagPrefix.doGenerateItem(material)) {
                        generateMaterialItem(tagPrefix, material, registrate);
                    }
                }
            }
        }
        MATERIAL_ITEMS = MATERIAL_ITEMS_BUILDER.build();
    }

    private static void generateMaterialItem(TagPrefix tagPrefix, Material material, BreaRegistrate registrate) {
        MATERIAL_ITEMS_BUILDER.put(tagPrefix, material, registrate
                .item(tagPrefix.idPattern().formatted(material.getName()),
                        properties -> new TagPrefixItem(properties, tagPrefix, material))
                .onRegister(TagPrefixItem::onRegister)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .transform(BreaItems.unificationItem(tagPrefix, material))
                .properties(p -> p.stacksTo(tagPrefix.maxStackSize()))
                .model(NonNullBiConsumer::noop)
                .onRegister(BreaItems::cauldronInteraction)
                .register());
    }
}
