package com.phasetranscrystal.brealib.mui.widget;

import com.phasetranscrystal.brealib.mui.GuiTexture;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;
import com.phasetranscrystal.brealib.mui.modular.WidgetUIAccess;
import com.phasetranscrystal.brealib.mui.widget.layout.Align;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

import com.google.common.base.Preconditions;
import icyllis.modernui.fragment.Fragment;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Accessors(chain = true)
public class Widget extends Fragment {

    @Getter
    protected ModularUI gui;
    @Setter
    protected WidgetUIAccess uiAccess;
    @Getter
    private Position parentPosition = Position.ORIGIN;
    @Getter
    private Position selfPosition;
    @Getter
    private Position position;
    @Getter
    private Size size;
    @Getter
    @Setter
    private Align align = Align.NONE;
    @Getter
    @Setter
    private boolean isActive;
    @Getter
    private boolean isFocus;
    @Getter
    protected boolean isClientSideWidget;
    @Getter
    protected final List<Component> tooltipTexts = new ArrayList<>();
    @Getter
    protected GuiTexture backgroundTexture;
    protected boolean drawBackgroundWhenHover = true;
    protected GuiTexture hoverTexture;
    @Getter
    @Setter
    protected GuiTexture overlay;
    @Getter
    protected WidgetGroup parent;
    @Getter
    protected boolean initialized;
    protected boolean tryToDrag = false;
    protected Supplier<Object> draggingProvider;
    protected BiFunction<Object, Position, GuiTexture> draggingRenderer;
    protected Predicate<Object> draggingAccept = o -> false;
    protected Consumer<Object> draggingIn;
    protected Consumer<Object> draggingOut;
    protected Consumer<Object> draggingSuccess;
    protected Object draggingElement;

    public Widget(Position selfPosition, Size size) {
        Preconditions.checkNotNull(selfPosition, "selfPosition");
        Preconditions.checkNotNull(size, "size");
        this.selfPosition = selfPosition;
        this.size = size;
        this.position = this.parentPosition.add(selfPosition);
        this.isActive = true;
    }

    public Widget(int x, int y, int width, int height) {
        this(new Position(x, y), new Size(width, height));
    }

    protected final void writeUpdateInfo(int id, Consumer<RegistryFriendlyByteBuf> buffer) {
        if (uiAccess != null && gui != null) {
            uiAccess.writeUpdateInfo(this, id, buffer);
        }
    }

    protected final void writeClientAction(int id, Consumer<RegistryFriendlyByteBuf> buffer) {
        if (uiAccess != null && !isClientSideWidget) {
            uiAccess.writeClientAction(this, id, buffer);
        }
    }

    public void setSelfPosition(Position selfPosition) {
        if (this.selfPosition.equals(selfPosition)) return;
        this.selfPosition = selfPosition;
        recomputePosition();
        if (isParent(parent)) {
            parent.onChildSelfPositionUpdate(this);
        }
    }

    protected void recomputePosition() {
        this.position = this.parentPosition.add(selfPosition);
        onPositionUpdate();
    }

    protected void onPositionUpdate() {}

    protected void onSizeUpdate() {}

    public boolean isParent(WidgetGroup widgetGroup) {
        if (parent == null) return false;
        if (parent == widgetGroup) return true;
        return parent.isParent(widgetGroup);
    }

    public void setSize(Size size) {
        if (this.size.equals(size)) return;
        this.size = size;
        onSizeUpdate();
        if (isParent(parent)) {
            parent.onChildSizeUpdate(this);
        }
    }

    public final void setSize(int width, int height) {
        setSize(new Size(width, height));
    }

    public final void setSizeWidth(int width) {
        setSize(width, getSize().height);
    }

    public final void setSizeHeight(int height) {
        setSize(getSize().width, height);
    }

    public final int getPositionX() {
        return getPosition().x;
    }

    public final int getPositionY() {
        return getPosition().y;
    }

    public final int getSizeWidth() {
        return getSize().width;
    }

    public final int getSizeHeight() {
        return getSize().height;
    }
}
