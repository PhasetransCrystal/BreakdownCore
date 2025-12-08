package com.phasetranscrystal.brealib.mui.widget;

import com.phasetranscrystal.brealib.mui.property.AlphaProperty;

import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.core.Context;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.Button;

import net.minecraft.client.Minecraft;

public class HoverAlphaButton extends Button {

    public static final Runnable CLOSE_SCREEN = () -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(null));
    public final ObjectAnimator hoverAnimator;

    public HoverAlphaButton(Context context) {
        super(context);
        hoverAnimator = ObjectAnimator.ofFloat(this, AlphaProperty.INSTANCE, 0.6F, 1F);
        hoverAnimator.setDuration(100);

        setClickable(true);
        setFocusable(true);
        setAlpha(0.6F);

        setOnHoverListener((view, event) -> switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER -> post(hoverAnimator::start);
            case MotionEvent.ACTION_HOVER_EXIT -> post(hoverAnimator::reverse);
            default -> false;
        });
    }
}
