package com.phasetranscrystal.brealib.mui.factory;

import com.phasetranscrystal.brealib.mui.modular.ModularUI;

import icyllis.modernui.mc.neoforge.MuiForgeApi;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

public abstract class UIFactory<T> {

    public final ResourceLocation uiFactoryId;
    public static final Map<ResourceLocation, UIFactory<?>> FACTORIES = new HashMap<>();

    public UIFactory(ResourceLocation uiFactoryId) {
        this.uiFactoryId = uiFactoryId;
    }

    public static void register(UIFactory<?> factory) {
        FACTORIES.put(factory.uiFactoryId, factory);
    }

    public final boolean openUI(T holder, Player player) {
        ModularUI uiTemplate = createUITemplate(holder, player);
        if (uiTemplate == null) return false;
        uiTemplate.initWidgets();
        if (player.containerMenu != player.inventoryMenu) {
            player.closeContainer();
        }
        RegistryFriendlyByteBuf serializedHolder = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
        writeHolderToSyncData(serializedHolder, holder);
        MuiForgeApi.openScreen(uiTemplate);
        return true;
    }

    public final void initClientUI(RegistryFriendlyByteBuf serializedHolder, int windowId) {}

    protected abstract ModularUI createUITemplate(T holder, Player entityPlayer);

    protected abstract T readHolderFromSyncData(RegistryFriendlyByteBuf syncData);

    protected abstract void writeHolderToSyncData(RegistryFriendlyByteBuf syncData, T holder);
}
