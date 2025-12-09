package com.phasetranscrystal.brealib.mui.preset.view;

import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.mc.MinecraftSurfaceView;
import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.RelativeLayout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.NotNull;

public abstract class ItemView extends RelativeLayout {

    public ItemStack stack;
    protected float hoverAlpha = 0;
    public final ObjectAnimator hoverAnimator = ObjectAnimator.ofFloat(this, hoverProperty, 0, 1);
    public final ItemLayerView itemLayer;

    public ItemView(Context context, ItemStack stack) {
        super(context);

        itemLayer = new ItemLayerView(context);
        addView(itemLayer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setStack(stack);

        setClickable(true);
        setOnTouchListener((v, e) -> {
            onClick(e);
            return false;
        });

        setOnHoverListener((view, flag) -> switch (flag.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER -> post(hoverAnimator::start);
            case MotionEvent.ACTION_HOVER_EXIT -> post(hoverAnimator::reverse);
            default -> false;
        });

        // this.setBackground(MUIHelper.withBorder());//test
    }

    // public abstract LayoutParams configureText(TextView text);

    public void setStack(ItemStack stack) {
        this.stack = stack;
        // itemLayer.invalidate();
    }

    // ---[Render Part 渲染部分]---

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int actuallyLos = Math.min(getWidth(), getHeight());
        float xAmend = (getWidth() - actuallyLos) / 2F;
        float yAmend = (getHeight() - actuallyLos) / 2F;

        drawContext(actuallyLos, xAmend, yAmend, canvas);
    }

    public abstract void drawContext(int actuallyLos, float x0, float y0, Canvas canvas);

    // ---[Interaction Part 交互部分]---

    protected void onClick(MotionEvent event) {
        int actionIndex = (Screen.hasShiftDown() ? -1 : 1) * event.getActionButton();
        this.stack = new ItemStack(Items.BEACON);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // SynMenuSlotClick.send(menu.getType(), slot.index, actionIndex);
        }

        // ContainerHelper.handleSlotClick(actionIndex, slot.index, Minecraft.getInstance().player, menu, true);
        // this.refresh();
    }

    public class ItemLayerView extends MinecraftSurfaceView implements MinecraftSurfaceView.Renderer {

        int width;
        int height;

        public ItemLayerView(Context context) {
            super(context);
            this.setRenderer(this);
        }

        @Override
        public void onSurfaceChanged(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void onDraw(@NotNull GuiGraphics gr, int mouseX, int mouseY, float deltaTick, double guiScale, float alpha) {
            int guiScaledWidth = (int) (width / guiScale);
            int guiScaledHeight = (int) (height / guiScale);
            gr.pose().pushMatrix();
            int x = guiScaledWidth / 2;
            int y = guiScaledHeight / 2;
            gr.renderItem(stack, x - 8, y - 8);
            gr.pose().translate(x, y);
            gr.pose().scale(0.8F);
            gr.drawCenteredString(Minecraft.getInstance().font, "" + stack.getCount(), 0, 4, 0xFFFFFFFF);
            gr.pose().popMatrix();
        }
    }

    public static final HoverProperty hoverProperty = new HoverProperty();

    public static class HoverProperty extends FloatProperty<ItemView> {

        public HoverProperty() {
            super("slot_hover");
        }

        @Override
        public void setValue(ItemView object, float value) {
            object.hoverAlpha = value;
            object.invalidate();
        }

        @Override
        public Float get(ItemView object) {
            return object.hoverAlpha;
        }
    }
}
