package com.phasetranscrystal.breacore.api.mui.editor;

import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.machine.MetaMachine;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EditableMachineUI implements IEditableUI<WidgetGroup, MetaMachine> {

    @Getter
    final String groupName;
    @Getter
    final ResourceLocation uiPath;
    final Supplier<WidgetGroup> widgetSupplier;
    final BiConsumer<WidgetGroup, MetaMachine> binder;
    @Nullable
    private CompoundTag customUICache;

    public EditableMachineUI(String groupName, ResourceLocation uiPath, Supplier<WidgetGroup> widgetSupplier,
                             BiConsumer<WidgetGroup, MetaMachine> binder) {
        this.groupName = groupName;
        this.uiPath = uiPath;
        this.widgetSupplier = widgetSupplier;
        this.binder = binder;
    }

    public WidgetGroup createDefault() {
        return widgetSupplier.get();
    }

    public void setupUI(WidgetGroup template, MetaMachine machine) {
        binder.accept(template, machine);
    }

    //////////////////////////////////////
    // ******** GUI *********//
    //////////////////////////////////////

    @Nullable
    public WidgetGroup createCustomUI() {
        if (hasCustomUI()) {
            var nbt = getCustomUI();
            var group = new WidgetGroup();
            // group.setSelfPosition(new Position(0, 0));
            return group;
        }
        return null;
    }

    public CompoundTag getCustomUI() {
        if (this.customUICache == null) {
            ResourceManager resourceManager = null;
            if (BreaUtil.isClientSide()) {
                resourceManager = Minecraft.getInstance().getResourceManager();
            } else if (BreaUtil.getMinecraftServer() != null) {
                resourceManager = BreaUtil.getMinecraftServer().getResourceManager();
            }
            if (resourceManager == null) {
                this.customUICache = new CompoundTag();
            } else {
                try {
                    var resource = resourceManager
                            .getResourceOrThrow(ResourceLocation.fromNamespaceAndPath(uiPath.getNamespace(),
                                    "ui/machine/%s.mui".formatted(uiPath.getPath())));
                    try (InputStream inputStream = resource.open()) {
                        try (DataInputStream dataInputStream = new DataInputStream(inputStream);) {
                            this.customUICache = NbtIo.read(dataInputStream, NbtAccounter.unlimitedHeap());
                        }
                    }
                } catch (Exception e) {
                    this.customUICache = new CompoundTag();
                }
                if (this.customUICache == null) {
                    this.customUICache = new CompoundTag();
                }
            }
        }
        return this.customUICache;
    }

    public boolean hasCustomUI() {
        return !getCustomUI().isEmpty();
    }

    public void reloadCustomUI() {
        this.customUICache = null;
    }
}
