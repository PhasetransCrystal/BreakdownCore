package com.phasetranscrystal.brealib.mui.modular;

import com.phasetranscrystal.brealib.mui.GuiTexture;
import com.phasetranscrystal.brealib.mui.widget.SlotWidget;
import com.phasetranscrystal.brealib.mui.widget.Widget;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class ModularUI extends Fragment {

    @Getter
    private final HashMap<Slot, SlotWidget> slotMap = new LinkedHashMap<>();
    public final WidgetGroup mainGroup;
    @Getter
    private int screenWidth, screenHeight;
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

    public void setFullScreen() {
        this.fullScreen = true;
        setSize(getScreenWidth(), getScreenHeight());
    }

    public void setSize(int width, int height) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            mainGroup.setSize(new Size(width, height));
        }
    }

    public void updateScreenSize(int screenWidth, int screenHeight) {
        if (fullScreen && (screenWidth != width || screenHeight != height)) {
            width = screenWidth;
            height = screenHeight;
            return;
        }
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        mainGroup.setParentPosition(Position.ORIGIN);
    }

    public void initWidgets() {
        mainGroup.setGui(this);
        mainGroup.initWidget();
    }

    public ModularUI widget(Widget widget) {
        Preconditions.checkNotNull(widget);
        mainGroup.addWidget(widget);
        return this;
    }

    public ModularUI background(GuiTexture texture) {
        mainGroup.setBackground(texture, new Rect(0, 0, 0, 0));
        return this;
    }

    public ModularUI background(GuiTexture texture, Rect rect) {
        mainGroup.setBackground(texture, rect);
        return this;
    }

    /**
     * 构建基本的UI容器组件
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup gameScreen, DataSet savedInstanceState) {
        var context = requireContext();
        var layout = new FrameLayout(context);

        var mg = mainGroup.createContainer(layout.getContext());
        mainGroup.drawBackground(mg);
        mainGroup.modifyView(mg);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                layout.dp(mainGroup.getSizeWidth()),
                layout.dp(mainGroup.getSizeHeight()));
        lp.gravity = Gravity.CENTER;

        layout.addView(mg, lp);

        return layout;
    }
}
