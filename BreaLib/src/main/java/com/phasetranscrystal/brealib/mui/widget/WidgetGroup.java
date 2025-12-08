package com.phasetranscrystal.brealib.mui.widget;

import com.phasetranscrystal.brealib.mui.modular.ModularUI;
import com.phasetranscrystal.brealib.mui.modular.WidgetUIAccess;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.AbsoluteLayout;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WidgetGroup extends Widget {

    public final List<Widget> widgets = new ArrayList<>();
    private final WidgetGroupUIAccess groupUIAccess = new WidgetGroupUIAccess();
    @Getter
    private boolean isDynamicSized;
    protected final List<Widget> waitToRemoved;
    protected final List<Widget> waitToAdded;

    public WidgetGroup() {
        this(0, 0, 50, 50);
    }

    public WidgetGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.isDynamicSized = false;
        waitToRemoved = new ArrayList<>();
        waitToAdded = new ArrayList<>();
    }

    public WidgetGroup(Position position) {
        super(position, Size.ZERO);
        this.isDynamicSized = true;
        waitToRemoved = new ArrayList<>();
        waitToAdded = new ArrayList<>();
    }

    public WidgetGroup(Position position, Size size) {
        super(position, size);
        this.isDynamicSized = false;
        waitToRemoved = new ArrayList<>();
        waitToAdded = new ArrayList<>();
    }

    public WidgetGroup setDynamicSized(boolean dynamicSized) {
        if (dynamicSized == this.isDynamicSized) return this;
        isDynamicSized = dynamicSized;
        recomputeSize();
        return this;
    }

    @Override
    public void setGui(ModularUI gui) {
        super.setGui(gui);
        for (Widget widget : widgets) {
            if (widget.gui != gui) {
                widget.setGui(gui);
            }
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        for (Widget widget : widgets) {
            if (widget.gui != gui) {
                widget.setGui(gui);
            }
            widget.initWidget();
        }
    }

    public WidgetGroup addWidget(Widget widget) {
        return addWidget(widgets.size(), widget);
    }

    public WidgetGroup addWidget(int index, Widget widget) {
        if (widget == this) {
            throw new IllegalArgumentException("Cannot add self");
        }
        if (widget == null) return this;
        if (widgets.contains(widget)) {
            throw new IllegalArgumentException("Already added");
        }
        this.widgets.add(index, widget);
        if (isClientSideWidget) {
            widget.setClientSideWidget();
        }
        widget.setUiAccess(groupUIAccess);
        if (widget.gui != gui) {
            widget.setGui(gui);
        }
        widget.setParent(this);
        widget.setParentPosition(getPosition());
        if (isInitialized() && !widget.isInitialized()) {
            widget.initWidget();
            if (!isRemote() && !widget.isClientSideWidget) {
                writeUpdateInfo(2, buffer -> {
                    buffer.writeVarInt(index);
                    widget.writeInitialData(buffer);
                });
            }
        }
        recomputeLayout();
        recomputeSize();
        return this;
    }

    @Override
    public WidgetGroup setClientSideWidget() {
        super.setClientSideWidget();
        for (Widget widget : widgets) {
            widget.setClientSideWidget();
        }
        return this;
    }

    public void waitToRemoved(Widget widget) {
        synchronized (waitToRemoved) {
            waitToRemoved.add(widget);
        }
    }

    public void waitToAdded(Widget widget) {
        synchronized (waitToAdded) {
            waitToAdded.add(widget);
        }
    }

    public int getAllWidgetSize() {
        return widgets.size() - waitToRemoved.size() + waitToAdded.size();
    }

    public void removeWidget(Widget widget) {
        if (!widgets.contains(widget)) {
            return;
        }
        this.widgets.remove(widget);
        widget.setUiAccess(null);
        widget.setGui(null);
        widget.setParentPosition(Position.ORIGIN);
        recomputeLayout();
        recomputeSize();
    }

    public void clearAllWidgets() {
        this.widgets.forEach(it -> {
            it.setUiAccess(null);
            it.setGui(null);
            it.setParentPosition(Position.ORIGIN);
        });
        this.widgets.clear();
        if (!waitToRemoved.isEmpty()) {
            synchronized (waitToRemoved) {
                waitToRemoved.clear();
            }
        }
        if (!waitToAdded.isEmpty()) {
            synchronized (waitToAdded) {
                waitToAdded.clear();
            }
        }
        recomputeLayout();
        recomputeSize();
    }

    @Override
    public AbsoluteLayout createContainer(Context context) {
        return new AbsoluteLayout(context);
    }

    @Override
    public void modifyView(ViewGroup container) {
        for (var widget : widgets) {
            if (!widget.isVisible()) {
                continue;
            }
            var ccon = widget.createContainer(container.getContext());
            widget.drawBackground(ccon);
            widget.modifyView(ccon);

            var size = widget.getSize();
            var position = widget.getPosition();
            var lp = new AbsoluteLayout.LayoutParams(container.dp(size.width), container.dp(size.height), container.dp(position.x), container.dp(position.y));

            container.addView(ccon, lp);
        }
    }

    protected void onChildSelfPositionUpdate(Widget child) {
        recomputeLayout();
        recomputeSize();
    }

    protected void onChildSizeUpdate(Widget child) {
        recomputeLayout();
        recomputeSize();
    }

    protected void recomputeLayout() {}

    protected boolean recomputeSize() {
        if (isDynamicSized) {
            Size currentSize = getSize();
            Size dynamicSize = computeDynamicSize();
            if (!currentSize.equals(dynamicSize)) {
                setSize(dynamicSize);
                return true;
            }
        }
        return false;
    }

    protected Size computeDynamicSize() {
        Position selfPosition = getPosition();
        Size currentSize = Size.ZERO;
        for (Widget widget : widgets) {
            Position size = widget.getPosition().add(widget.getSize()).subtract(selfPosition);
            if (size.x > currentSize.width) {
                currentSize = new Size(size.x, currentSize.height);
            }
            if (size.y > currentSize.height) {
                currentSize = new Size(currentSize.width, size.y);
            }
        }
        return currentSize;
    }

    @Override
    protected void onPositionUpdate() {
        Position selfPosition = getPosition();
        for (Widget widget : widgets) {
            widget.setParentPosition(selfPosition);
        }
        recomputeSize();
    }

    private class WidgetGroupUIAccess implements WidgetUIAccess {

        @Override
        public boolean attemptMergeStack(ItemStack itemStack, boolean fromContainer, boolean simulate) {
            WidgetUIAccess uiAccess = WidgetGroup.this.uiAccess;
            if (uiAccess != null) {
                return uiAccess.attemptMergeStack(itemStack, fromContainer, simulate);
            }
            return false;
        }

        @Override
        public void writeClientAction(Widget widget, int updateId, Consumer<RegistryFriendlyByteBuf> dataWriter) {
            WidgetGroup.this.writeClientAction(1, buffer -> {
                buffer.writeVarInt(widgets.indexOf(widget));
                buffer.writeVarInt(updateId);
                dataWriter.accept(buffer);
            });
        }

        @Override
        public void writeUpdateInfo(Widget widget, int updateId, Consumer<RegistryFriendlyByteBuf> dataWriter) {
            WidgetGroup.this.writeUpdateInfo(1, buffer -> {
                buffer.writeVarInt(widgets.indexOf(widget));
                buffer.writeVarInt(updateId);
                dataWriter.accept(buffer);
            });
        }
    }
}
