package com.phasetranscrystal.brealib.mui.preset.view;

import com.phasetranscrystal.brealib.mui.MuiHelper;
import com.phasetranscrystal.brealib.mui.PublicTexture;
import com.phasetranscrystal.brealib.mui.texture.GuiTexture;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

public class SimpleItemView extends ItemView {

    public static final int SIZE = 40;
    public static final GuiTexture bg = PublicTexture.BG_ITEM_SIMPLE;

    public final Paint bottomBarPaint = new Paint();

    public SimpleItemView(Context context, ItemStack stack) {
        super(context, stack);
        MuiHelper.setTestingBoarder(this);
    }

    // @Override
    public LayoutParams configureText(TextView text) {
        text.setTextSize(10);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0, 0, 0, dp(SIZE / 8F));
        return params;
    }

    @Override
    public void drawContext(int actuallyLos, float x0, float y0, Canvas canvas) {
        if (stack.isEmpty()) {
            drawSlotBackground(canvas, x0, y0, actuallyLos, getAlpha());
        } else {
            canvas.drawImage(bg.getTexture().get(), null, new RectF(x0, y0, x0 + actuallyLos, y0 + actuallyLos), null);
            canvas.save();
            float f = actuallyLos / 16F;
            if (stack.getDamageValue() > 0) {
                canvas.clipRect(x0, y0 + actuallyLos - f, x0 + actuallyLos * (1 - ((float) stack.getDamageValue()) / stack.getMaxDamage()), y0 + actuallyLos);
                bottomBarPaint.setColor(stack.getBarColor());
                canvas.drawRoundRect(x0, y0 + actuallyLos - 2 * f, x0 + actuallyLos, y0 + actuallyLos, f, f, f, f, bottomBarPaint);
            } else {
                TextColor textColor = stack.getDisplayName().getStyle().getColor();
                if (textColor != null) {
                    canvas.clipRect(x0, y0 + actuallyLos - f, x0 + actuallyLos, y0 + actuallyLos);
                    bottomBarPaint.setColor(textColor.getValue());
                    canvas.drawRoundRect(x0, y0 + actuallyLos - 2 * f, x0 + actuallyLos, y0 + actuallyLos, f, f, f, f, bottomBarPaint);
                }
            }
            canvas.restore();
        }
        drawSlotForeground(canvas, x0, y0, actuallyLos, hoverAlpha);
    }

    private static final Paint BACKGROUND_PAINTER_STROKE = new Paint();
    private static final Paint BACKGROUND_PAINTER_FILL = new Paint();

    static {
        BACKGROUND_PAINTER_STROKE.setColor(0xFF707070);
        BACKGROUND_PAINTER_STROKE.setStroke(true);
        BACKGROUND_PAINTER_STROKE.setStrokeWidth(2);
        BACKGROUND_PAINTER_FILL.setColor(0x33000000);
    }

    public static void drawSlotBackground(Canvas canvas, float x, float y, int a, float alpha) {
        drawSlotBackground(canvas, new RectF(x, y, x + a, y + a), alpha);
    }

    public static void drawSlotBackground(Canvas canvas, RectF range, float alpha) {
        if (alpha == 0) return;
        float r = range.width() / 16;
        BACKGROUND_PAINTER_STROKE.setAlphaF(alpha);
        BACKGROUND_PAINTER_FILL.setAlphaF(alpha * 0.2F);
        canvas.drawRoundRect(range, r, BACKGROUND_PAINTER_FILL);
        canvas.drawRoundRect(range, r, BACKGROUND_PAINTER_STROKE);
    }

    private static final Paint FOREGROUND_PAINTER_STROKE = new Paint();
    private static final Paint FOREGROUND_PAINTER_FILL = new Paint();

    static {
        FOREGROUND_PAINTER_STROKE.setColor(0xEEEEEE);
        FOREGROUND_PAINTER_STROKE.setStroke(true);
        FOREGROUND_PAINTER_STROKE.setStrokeWidth(2);
        FOREGROUND_PAINTER_FILL.setColor(0xBBBBBB);
    }

    public static void drawSlotForeground(Canvas canvas, float x, float y, int a, float alpha) {
        drawSlotForeground(canvas, new RectF(x, y, x + a, y + a), alpha);
    }

    public static void drawSlotForeground(Canvas canvas, RectF range, float alpha) {
        if (alpha == 0) return;
        float r = range.width() / 16;
        FOREGROUND_PAINTER_STROKE.setAlphaF(alpha);
        FOREGROUND_PAINTER_FILL.setAlphaF(alpha * 0.3F);
        canvas.drawRoundRect(range, r, FOREGROUND_PAINTER_FILL);
        canvas.drawRoundRect(range, r, FOREGROUND_PAINTER_STROKE);
    }
}
