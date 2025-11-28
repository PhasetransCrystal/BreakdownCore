package com.phasetranscrystal.breacore.data.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate.Brea;

public class BreaCreativeModeTabs {

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DEBUG_ITEMS;

    static {
        DEBUG_ITEMS = Brea.defaultCreativeTab("debug_items", builder -> builder
                .title(Component.translatable("itemsGroup.breacore.debug_items"))
                .icon(() -> Items.COMMAND_BLOCK.asItem().getDefaultInstance())
                .build())
                .register();
    }

    public static void init() {}
}
