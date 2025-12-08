package com.phasetranscrystal.brealib.mui.preset;

import com.phasetranscrystal.brealib.mui.MuiHelper;
import com.phasetranscrystal.brealib.mui.PublicTexture;
import com.phasetranscrystal.brealib.mui.property.AlphaProperty;

import icyllis.modernui.animation.*;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.*;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.*;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Range;

public abstract class NarrowPaginationAEFView extends RootAEFView {

    public static final int PAGINATION_WIDTH = (int) (RootAEFView.TOP_BOTTOM_HEIGHT * 0.625F);
    public static final int PAGINATION_BUTTON_HEIGHT = PAGINATION_WIDTH * 3;
    public static final int PAGINATION_EDGE_DIST = 3;

    protected int paginationWidth;
    protected int paginationButtonHeight;
    protected int paginationEdgeDist;
    protected int paginationCR;
    protected PaginationGroup paginationGroup;

    protected View mainView;
    protected RelativeLayout.LayoutParams mainViewLayoutParams;
    protected ObjectAnimator mainViewAnimator;

    private AniListener aniListener;

    public NarrowPaginationAEFView(Context context, ResourceLocation icon, String title) {
        super(context, icon, title);
    }

    @Override
    public void init() {
        super.init();
        paginationWidth = dp(PAGINATION_WIDTH);
        paginationButtonHeight = dp(PAGINATION_BUTTON_HEIGHT);
        paginationEdgeDist = dp(PAGINATION_EDGE_DIST);
        paginationCR = paginationWidth / 2 - paginationEdgeDist;

        mainViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainViewLayoutParams.setMargins(paginationWidth, 0, 0, 0);

        mainViewAnimator = ObjectAnimator.ofFloat(null, AlphaProperty.INSTANCE, 0, 1);
        mainViewAnimator.setDuration(100);
        aniListener = new AniListener();
        mainViewAnimator.addListener(aniListener);
    }

    @Override
    public void createCenterLayout(RelativeLayout centerLayout) {
        paginationGroup = new PaginationGroup(getContext());
        paginationGroup.setOrientation(LinearLayout.VERTICAL);
        paginationGroup.setGravity(Gravity.CENTER);
        MuiHelper.setTestingBoarder(paginationGroup);
        centerLayout.addView(paginationGroup, new LinearLayout.LayoutParams(paginationWidth, ViewGroup.LayoutParams.MATCH_PARENT));

        for (int i = 1; i <= getPaginationCount(); i++) {
            paginationGroup.addView(new PageButton(getContext(), 10000 + i, getPaginationIcon(i), i));
        }
        if (getPaginationCount() > 0) {
            paginationGroup.preCheck(10001);
            mainView = createView(1);
            mainViewAnimator.setTarget(mainView);
            centerLayout.addView(mainView, mainViewLayoutParams);
            paginationGroup.setOnCheckedChangeListener((group, id) -> {
                // if (!init) return;
                if (paginationGroup.animator.isRunning()) paginationGroup.animator.cancel();
                PageButton view = findViewById(id);
                paginationGroup.animator.setValues(PropertyValuesHolder.ofInt(PaginationGroup.property, paginationGroup.flagY, view.getTop() + view.getHeight() / 2));
                paginationGroup.animator.start();
                aniListener.toIndex = id - 10000;
                mainViewAnimator.reverse();
            });
        } else {
            centerLayout.addView(createView(0), mainViewLayoutParams);
        }
    }

    @Range(from = 0, to = 4)
    public abstract int getPaginationCount();

    public abstract View createView(int index);

    public abstract ResourceLocation getPaginationIcon(int index);

    public class PaginationGroup extends RadioGroup {

        public static final int CHECKED_BG_COLOR = 0xDF2C2C2C;
        private final Paint paint = new Paint();

        private int flagY;
        private final ObjectAnimator animator;

        private boolean init = false;

        public PaginationGroup(Context context) {
            super(context);
            setWillNotDraw(false);
            paint.setColor(CHECKED_BG_COLOR);
            animator = ObjectAnimator.ofInt(this, property, 0);
            animator.setDuration(250);
            animator.setInterpolator(TimeInterpolator.DECELERATE);

            OneShotPreDrawListener.add(this, () -> {
                if (this.getChildCount() >= 1 && this.getCheckedId() != View.NO_ID) {
                    PageButton view = findViewById(getCheckedId());
                    this.flagY = view.getHeight() / 2 + view.getTop();
                    mainViewAnimator.start();
                    // init = true;
                }
            });
        }

        @Override
        protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
            return new LinearLayout.LayoutParams(paginationWidth, paginationButtonHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            MuiHelper.imageMesh((PublicTexture.ROOT_LEFT_DEC.getTexture().get()), canvas,
                    0, 0, getWidth(), getWidth(), getWidth(), getHeight(), false);
            if (this.getChildCount() >= 1) {
                canvas.drawRoundRect(paginationEdgeDist, flagY - paginationButtonHeight / 2, getWidth() - paginationEdgeDist, flagY + paginationButtonHeight / 2,
                        paginationCR, paginationCR, paginationCR, paginationCR, paint);
            }
        }

        public void preCheck(int id) {
            this.check(id);
            if (findViewById(id) instanceof PageButton b) {
                b.paint.setColor(PageButton.COLOR_CHECKED);
            }
        }

        public static final PosProperty property = new PosProperty();

        public static class PosProperty extends IntProperty<PaginationGroup> {

            public PosProperty() {
                super("pagination_moving_animator");
            }

            @Override
            public void setValue(PaginationGroup object, int value) {
                object.flagY = value;
                object.invalidate();
            }

            @Override
            public Integer get(PaginationGroup object) {
                return object.flagY;
            }
        }
    }

    public static class PageButton extends RadioButton {

        public static final int COLOR_CHECKED = 0xFFD5D3CF;
        // public static final int COLOR_UNCHECKED = 0xBFAFB1B3;
        public static final int COLOR_UNCHECKED = 0x9FFF0000;

        public final Image icon;
        public final int index;
        public final ObjectAnimator textureColorAnimator;
        private final Paint paint = new Paint();

        {
            paint.setColor(COLOR_UNCHECKED);
        }

        private PageButton(Context context, int id, ResourceLocation imgLoc, int index) {
            this(context, id, Image.create(imgLoc.getNamespace(), imgLoc.getPath()), index);
        }

        private PageButton(Context context, int id, Image icon, int index) {
            super(context, null, null, null);
            setId(id);
            setFocusable(true);
            setClickable(true);
            this.icon = icon;
            this.index = index;

            textureColorAnimator = ObjectAnimator.ofArgb(this, colorProperty, COLOR_UNCHECKED, COLOR_CHECKED);
            setOnHoverListener((view, event) -> switch (event.getAction()) {
                case MotionEvent.ACTION_HOVER_ENTER -> !isChecked() && post(textureColorAnimator::start);
                case MotionEvent.ACTION_HOVER_EXIT -> !isChecked() && post(textureColorAnimator::reverse);
                default -> false;
            });

            setOnCheckedChangeListener((view, check) -> {
                if (!check) post(textureColorAnimator::reverse);
            });
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int v = (getHeight() - getWidth()) / 2;
            float margin = getWidth() * 0.2F;
            canvas.drawImage(icon, null, new RectF(margin, v + margin, getWidth() - margin, v + getWidth() - margin), paint);
        }

        private static final ColorProperty colorProperty = new ColorProperty();

        public static class ColorProperty extends IntProperty<PageButton> {

            public ColorProperty() {
                super("button_texture_color");
            }

            @Override
            public void setValue(PageButton object, int value) {
                object.paint.setColor(value);
                object.invalidate();
            }

            @Override
            public Integer get(PageButton object) {
                return object.paint.getColor();
            }
        }
    }

    private class AniListener implements AnimatorListener {

        public int toIndex;

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {
            if (isReverse) {
                removeView(mainView);
                mainView = createView(toIndex);
                mainView.setAlpha(0);
                mainViewAnimator.setTarget(mainView);
                centerLayout.addView(mainView, mainViewLayoutParams);
                mainViewAnimator.start();
            }
        }
    }
}
