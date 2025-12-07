package com.phasetranscrystal.brealib.mui.widget;

import com.phasetranscrystal.brealib.mui.modular.WidgetUIAccess;
import com.phasetranscrystal.brealib.mui.widget.layout.Layout;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WidgetGroup extends Widget {

    public final List<Widget> widgets = new ArrayList<>();
    private final WidgetGroupUIAccess groupUIAccess = new WidgetGroupUIAccess();
    @Getter
    private boolean isDynamicSized;
    @Getter
    private Layout layout = Layout.NONE;
    @Getter
    private int layoutPadding = 0;
    @Getter
    @Setter
    private boolean allowXEIIngredientOverMouse = true;
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

    protected void onChildSizeUpdate(Widget child) {
        recomputeLayout();
        recomputeSize();
    }

    protected void onChildSelfPositionUpdate(Widget child) {
        recomputeLayout();
        recomputeSize();
    }

    protected void recomputeLayout() {
        if (layout != Layout.NONE) {
            var lastPosition = new Position(0, 0);
            switch (layout) {
                case VERTICAL_LEFT -> {
                    for (var widget : widgets) {
                        lastPosition = lastPosition.addY(layoutPadding);
                        widget.setSelfPosition(lastPosition);
                        lastPosition = lastPosition.add(0, widget.getSizeHeight());
                    }
                }
                case VERTICAL_CENTER -> {
                    for (var widget : widgets) {
                        lastPosition = lastPosition.addY(layoutPadding);
                        widget.setSelfPosition(lastPosition.add((getSizeWidth() - widget.getSizeWidth()) / 2, 0));
                        lastPosition = lastPosition.add(0, widget.getSizeHeight());
                    }
                }
                case VERTICAL_RIGHT -> {
                    for (var widget : widgets) {
                        lastPosition = lastPosition.addY(layoutPadding);
                        widget.setSelfPosition(lastPosition.add(getSizeWidth() - widget.getSizeWidth(), 0));
                        lastPosition = lastPosition.add(0, widget.getSizeHeight());
                    }
                }
                case HORIZONTAL_TOP -> {
                    for (var widget : widgets) {
                        lastPosition = lastPosition.addX(layoutPadding);
                        widget.setSelfPosition(lastPosition);
                        lastPosition = lastPosition.add(widget.getSizeWidth(), 0);
                    }
                }
                case HORIZONTAL_CENTER -> {
                    for (var widget : widgets) {
                        lastPosition = lastPosition.addX(layoutPadding);
                        widget.setSelfPosition(lastPosition.add(0, (getSizeHeight() - widget.getSizeHeight()) / 2));
                        lastPosition = lastPosition.add(widget.getSizeWidth(), 0);
                    }
                }
                case HORIZONTAL_BOTTOM -> {
                    for (var widget : widgets) {
                        lastPosition = lastPosition.addX(layoutPadding);
                        widget.setSelfPosition(lastPosition.add(0, getSizeHeight() - widget.getSizeHeight()));
                        lastPosition = lastPosition.add(widget.getSizeWidth(), 0);
                    }
                }
            }
        }
    }

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
