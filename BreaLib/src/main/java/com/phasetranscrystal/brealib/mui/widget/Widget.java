package com.phasetranscrystal.brealib.mui.widget;

import com.phasetranscrystal.brealib.mui.GuiTexture;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;
import com.phasetranscrystal.brealib.mui.modular.WidgetUIAccess;
import com.phasetranscrystal.brealib.mui.widget.layout.Align;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;

import net.minecraft.network.RegistryFriendlyByteBuf;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class Widget {

    @Setter
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
    protected WidgetGroup parent;
    @Getter
    @Setter
    private Align align = Align.NONE;
    @Getter
    protected boolean isClientSideWidget;
    @Getter
    @Setter
    private boolean isVisible;
    @Getter
    @Setter
    private boolean isActive;
    @Getter
    protected boolean initialized;

    public Widget(Position selfPosition, Size size) {
        Preconditions.checkNotNull(selfPosition, "selfPosition");
        Preconditions.checkNotNull(size, "size");
        this.selfPosition = selfPosition;
        this.size = size;
        this.position = this.parentPosition.add(selfPosition);
        this.isVisible = true;
        this.isActive = true;
    }

    public Widget(int width, int height) {
        this(Position.ORIGIN, new Size(width, height));
    }

    public Widget(int x, int y, int width, int height) {
        this(new Position(x, y), new Size(width, height));
    }

    public void initTemplate() {}

    public Rect getRect() {
        return new Rect(position.x, position.y, size.width + position.x, size.height + position.y);
    }

    /**
     * Called on both sides to initialize widget data
     */
    public void initWidget() {
        initialized = true;
    }

    public void writeInitialData(RegistryFriendlyByteBuf buffer) {}

    public void readInitialData(RegistryFriendlyByteBuf buffer) {}

    protected void setParent(WidgetGroup parent) {
        this.parent = parent;
    }

    public ViewGroup createContainer(Context context) {
        return new FrameLayout(context);
    }

    public Widget setBackground(GuiTexture texture, Rect rect) {
        return this;
    }

    public boolean isRemote() {
        return (gui != null && gui.holder != null) ? gui.holder.isRemote() : BreaUtil.isRemote();
    }

    public void drawBackground(ViewGroup container) {}

    public void modifyView(ViewGroup container) {}

    public Widget setClientSideWidget() {
        isClientSideWidget = true;
        return this;
    }

    public void setParentPosition(Position parentPosition) {
        this.parentPosition = parentPosition;
        recomputePosition();
    }

    public void setSelfPosition(Position selfPosition) {
        if (this.selfPosition.equals(selfPosition)) return;
        this.selfPosition = selfPosition;
        recomputePosition();
        if (isParent(parent)) {
            parent.onChildSelfPositionUpdate(this);
        }
    }

    public final void setSelfPosition(int x, int y) {
        setSelfPosition(new Position(x, y));
    }

    public final void setSelfPositionX(int x) {
        setSelfPosition(x, getSelfPosition().y);
    }

    public final void setSelfPositionY(int y) {
        setSelfPosition(getSelfPosition().x, y);
    }

    public Position addSelfPosition(int addX, int addY) {
        setSelfPosition(new Position(selfPosition.x + addX, selfPosition.y + addY));
        return this.selfPosition;
    }

    public final int getSelfPositionX() {
        return getSelfPosition().x;
    }

    public final int getSelfPositionY() {
        return getSelfPosition().y;
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

    public boolean isParent(WidgetGroup widgetGroup) {
        if (parent == null) return false;
        if (parent == widgetGroup) return true;
        return parent.isParent(widgetGroup);
    }

    protected void recomputePosition() {
        this.position = this.parentPosition.add(selfPosition);
        onPositionUpdate();
    }

    protected void onPositionUpdate() {}

    protected void onSizeUpdate() {}

    /**
     * Read data received from server's {@link #writeUpdateInfo}
     */
    public void readUpdateInfo(int id, RegistryFriendlyByteBuf buffer) {}

    public void handleClientAction(int id, RegistryFriendlyByteBuf buffer) {}

    /**
     * Writes data to be sent to client's {@link #readUpdateInfo}
     */
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
}
