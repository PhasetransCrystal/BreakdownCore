package com.phasetranscrystal.brealib.mui.modular;

import com.phasetranscrystal.brealib.mui.widget.SlotWidget;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class ModularUI {

    @Getter
    private final HashMap<Slot, SlotWidget> slotMap = new LinkedHashMap<>();
    public final WidgetGroup mainGroup;
    @Getter
    private int width, height;
    @Getter
    private boolean fullScreen;
    private final List<Runnable> uiCloseCallback;
    @Getter
    private long tickCount;

    /**
     * UIHolder of this modular UI
     */
    public final IUIHolder holder;
    public final Player entityPlayer;

    public ModularUI(Size size, IUIHolder holder, Player entityPlayer) {
        this(new WidgetGroup(Position.ORIGIN, size), holder, entityPlayer);
    }

    public ModularUI(int width, int height, IUIHolder holder, Player entityPlayer) {
        this(new Size(width, height), holder, entityPlayer);
    }

    public ModularUI(WidgetGroup mainGroup, IUIHolder holder, Player entityPlayer) {
        this.mainGroup = mainGroup;
        mainGroup.setSelfPosition(Position.ORIGIN);
        this.width = mainGroup.getSize().width;
        this.height = mainGroup.getSize().height;
        this.holder = holder;
        this.entityPlayer = entityPlayer;
        this.uiCloseCallback = new ArrayList<>();
    }

    public ModularUI(IUIHolder holder, Player entityPlayer) {
        this(0, 0, holder, entityPlayer);
        fullScreen = true;
    }
}
