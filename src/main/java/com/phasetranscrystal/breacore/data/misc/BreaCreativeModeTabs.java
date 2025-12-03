package com.phasetranscrystal.breacore.data.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import com.tterrag.registrate.util.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

public class BreaCreativeModeTabs {

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DEBUG_ITEMS;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUID;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEM;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_BLOCK;

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DECORATION;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TOOL;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINE;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> ITEM;

    static {
        DEBUG_ITEMS = REGISTRATE.defaultCreativeTab("debug_items",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("debug_items", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemsGroup", BreaUtil.byPath("debug_items"), "Debug Page"))
                        .build())
                .register();
        MATERIAL_FLUID = REGISTRATE.defaultCreativeTab("material_fluid",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_fluid", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemGroup", BreaUtil.byPath("material_fluid"), BreaCore.NAME + " Material Fluid Containers"))
                        .build())
                .register();
        MATERIAL_ITEM = REGISTRATE.defaultCreativeTab("material_item",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_item", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemGroup", BreaUtil.byPath("material_item"), BreaCore.NAME + " Material Items"))
                        .build())
                .register();
        MATERIAL_BLOCK = REGISTRATE.defaultCreativeTab("material_block",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_block", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemGroup", BreaUtil.byPath("material_block"), BreaCore.NAME + " Material Blocks"))
                        .build())
                .register();
        DECORATION = REGISTRATE.defaultCreativeTab("decoration",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("decoration", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemGroup", BreaUtil.byPath("decoration"), BreaCore.NAME + " Decoration Blocks"))
                        .build())
                .register();
        TOOL = REGISTRATE.defaultCreativeTab("tool",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("tool", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemGroup", BreaUtil.byPath("tool"), BreaCore.NAME + " Tools"))
                        .build())
                .register();
        MACHINE = REGISTRATE.defaultCreativeTab("machine",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("machine", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemGroup", BreaUtil.byPath("machine"), BreaCore.NAME + " Machines"))
                        .build())
                .register();
        ITEM = REGISTRATE.defaultCreativeTab("item",
                builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("item", REGISTRATE))
                        .icon(Items.COMMAND_BLOCK::getDefaultInstance)
                        .title(REGISTRATE.addLang("itemGroup", BreaUtil.byPath("item"), BreaCore.NAME + " Items"))
                        .build())
                .register();
    }

    public static void init() {}

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        public final String name;
        public final BreaRegistrate registrate;

        public RegistrateDisplayItemsGenerator(String name, BreaRegistrate registrate) {
            this.name = name;
            this.registrate = registrate;
        }

        @Override
        public void accept(@NotNull CreativeModeTab.ItemDisplayParameters itemDisplayParameters,
                           @NotNull CreativeModeTab.Output output) {
            var tab = registrate.get(name, Registries.CREATIVE_MODE_TAB);
            for (var entry : registrate.getAll(Registries.BLOCK)) {
                if (!registrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get().asItem();
                if (item == Items.AIR)
                    continue;
            }
            for (var entry : registrate.getAll(Registries.ITEM)) {
                if (!registrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                output.accept(item);

            }
        }
    }
}
