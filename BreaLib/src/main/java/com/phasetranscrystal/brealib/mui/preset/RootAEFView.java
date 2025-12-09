package com.phasetranscrystal.brealib.mui.preset;

import com.phasetranscrystal.brealib.mui.MuiHelper;
import com.phasetranscrystal.brealib.mui.PublicTexture;
import com.phasetranscrystal.brealib.mui.preset.view.HoverAlphaButton;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.*;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RootAEFView extends RelativeLayout {

    public static final int COLOR_ICON_BACKGROUND = 0xFFC1C1C4;
    public static final int COLOR_DIVIDING_LINE = 0xFF9F9F9F;
    public static final int COLOR_TOP = 0xFFD3D3D3;
    public static final int COLOR_CENTER = 0x605C5C5C;
    public static final int COLOR_LOWER = 0xB82A2A2A;
    public static final int COLOR_BOTTOM = 0xDF222222;

    public static final int[] GRADIENT_COLOR = new int[] { COLOR_TOP, COLOR_CENTER, COLOR_LOWER, COLOR_LOWER };
    public static final float[] GRADIENT_POSITON = new float[] { 0F, 0.15F, 0.6F, 1F };

    public static final int COLOR_TITLE_TEXT = 0xFF4A4C4B;

    public static final int TOP_BOTTOM_HEIGHT = 40;
    public static final int EDGE_R = 10;

    public final ResourceLocation iconPath;
    public final String titleKey;
    public final Image icon;
    protected final Paint paint;
    protected final Paint gradientPaint;

    public final int tbHeight;
    public final int r;

    protected final CloseButton closeButton;
    protected final LinearLayout titleLayout;
    protected final RelativeLayout centerLayout;
    protected final LinearLayout bottomLayout;

    public RootAEFView(Context context, ResourceLocation icon, String title) {
        super(context);
        this.iconPath = icon;
        this.titleKey = title;
        this.icon = icon == null ? null : Image.create(icon.getNamespace(), icon.getPath());
        this.paint = new Paint();
        this.gradientPaint = new Paint();

        setWillNotDraw(false);

        tbHeight = dp(TOP_BOTTOM_HEIGHT);
        r = dp(EDGE_R);

        init();

        // 关闭按钮
        LayoutParams closeButtonParams = new LayoutParams((int) (tbHeight * 0.6F), (int) (tbHeight * 0.6F));
        closeButtonParams.setMarginsRelative(0, (int) (tbHeight * 0.2F), (int) (tbHeight * 0.2F), 0);
        closeButtonParams.addRule(ALIGN_PARENT_RIGHT);
        closeButtonParams.addRule(ALIGN_PARENT_TOP);
        closeButton = new CloseButton(getContext());
        addView(closeButton, closeButtonParams);

        // 顶部标题条
        LayoutParams titleLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleLayoutParams.setMargins((int) (tbHeight * 1.2F), (int) (tbHeight * 0.3F), 0, 0);
        titleLayoutParams.addRule(ALIGN_PARENT_LEFT);
        titleLayoutParams.addRule(ALIGN_PARENT_TOP);
        titleLayout = new LinearLayout(getContext());
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.setDividerPadding((int) (tbHeight * 0.15F));
        titleLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleLayout.setGravity(Gravity.BOTTOM);
        ShapeDrawable divider = new ShapeDrawable();
        divider.setShape(ShapeDrawable.VLINE);
        divider.setColor(COLOR_DIVIDING_LINE);
        divider.setSize(dp(1), (int) (tbHeight * 0.7F));
        titleLayout.setDividerDrawable(divider);
        addView(titleLayout, titleLayoutParams);
        titleLayout.addView(createMainTitleView());
        View secondaryTitleView = createSecondaryTitleView();
        if (secondaryTitleView != null) {
            addView(secondaryTitleView, secondaryTitleView.getLayoutParams());
        }

        // 中部布局
        centerLayout = new RelativeLayout(getContext());
        createCenterLayout(centerLayout);
        MuiHelper.setTestingBoarder(centerLayout);
        LayoutParams centerOuterLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        centerOuterLayoutParams.setMargins(0, tbHeight, 0, tbHeight);
        addView(centerLayout, centerOuterLayoutParams);

        // 底部布局
        bottomLayout = new LinearLayout(getContext()) {

            @Override
            protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                int v = (int) (tbHeight * 0.125F);
                param.setMargins(v, v, v, v);
                return param;
            }
        };
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.setGravity(Gravity.RIGHT);
        MuiHelper.setTestingBoarder(bottomLayout);
        bottomLayout.setWillNotDraw(false);
        createBottomButtons(bottomLayout);
        LayoutParams bottomLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, tbHeight);
        bottomLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        addView(bottomLayout, bottomLayoutParams);
    }

    /**
     * 初始化内容，在子组件添加前触发。
     * <p>
     * 由于所有子内容添加均在根类构造方法内完成，子类常量可能无法正常初始化。请在此方法中初始化数据。
     */
    public void init() {}

    /**
     * 创建主标题组件
     */
    public View createMainTitleView() {
        TextView mainTitleView = new TextView(getContext());
        mainTitleView.setText(I18n.get(titleKey));
        mainTitlePreset(mainTitleView, tbHeight);
        return mainTitleView;
    }

    /**
     * 创建副标题组件
     *
     * @return 创建的副标题组件。可为空，表示没有副标题。
     */
    public @Nullable View createSecondaryTitleView() {
        // TextView secondTitleView = new TextView(getContext());
        // secondTitleView.setText("TEST TEXT");
        // secondaryTitlePreset(secondTitleView,tbHeight);
        // return secondTitleView;
        return null;
    }

    /**
     * 创建底部按钮组，从右到左排列。
     * TODO 这一方法可能会在之后变动以规范化按钮组件。
     */
    public void createBottomButtons(LinearLayout bottomLayout) {}

    /**
     * 为文本组件预设为主标题形式
     */
    public static void mainTitlePreset(TextView title, int tbHeight) {
        title.setTextColor(COLOR_TITLE_TEXT);
        title.setTextSize(18);
        title.setPadding(0, 0, (int) (tbHeight * 0.1F), (int) (tbHeight * 0.075F));
    }

    /**
     * 为文本组件预设为副标题形式
     */
    public static void secondaryTitlePreset(TextView title, int tbHeight) {
        title.setTextColor(COLOR_DIVIDING_LINE);
        title.setTextSize(12);
        title.setPadding((int) (tbHeight * 0.1F), 0, (int) (tbHeight * 0.1F), (int) (tbHeight * 0.075F));
    }

    @Override
    @SuppressWarnings("all")
    public void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);

        // 绘制左上角区域
        paint.setColor(COLOR_ICON_BACKGROUND);
        canvas.drawRoundRect(0, 0, tbHeight, tbHeight, r, 0, 0, 0, paint);
        if (icon != null) {
            canvas.drawImage(icon, null, new RectF(0, 0, tbHeight, tbHeight), null);
        }

        // 绘制顶部区域
        paint.setColor(COLOR_TOP);
        canvas.drawRoundRect(tbHeight, 0, getWidth(), tbHeight, 0, r, 0, 0, paint);

        // 绘制标题内容
        // paint.setColor(COLOR_TITLE_TEXT);
        // paint.setTextSize(0.5F * tbHeight);
        // canvas.drawSimpleText(I18n.get(titleKey),
        // FontFamily.getSystemFontWithAlias("微软雅黑").getClosestMatch(FontPaint.NORMAL),
        // tbHeight * 1.2F, tbHeight * 0.8F, paint);
        canvas.drawImage(PublicTexture.ROOT_NAME_DEC.getTexture().get(), null,
                new RectF(tbHeight * 1.2F, tbHeight * 0.15F, tbHeight * 2.2F, tbHeight * 0.35F), null);

        // 绘制中部渐变区域
        canvas.drawRect(0, tbHeight, getWidth(), getHeight() - tbHeight, gradientPaint);

        // //绘制左侧修饰纹理
        // MuiHelper.imageMesh(PublicTexture.getPublicImage(PublicTexture.TEXTURE_LEFT_DEC), canvas,
        // 0, tbHeight, leftWidth, leftWidth, leftWidth, getHeight() - 2 * tbHeight);

        // 绘制底部区域
        paint.setColor(COLOR_BOTTOM);
        canvas.drawRoundRect(0, getHeight() - tbHeight, getWidth(), getHeight(), 0, 0, r, r, paint);

        // 绘制区域分界线
        paint.setColor(COLOR_DIVIDING_LINE);
        canvas.drawLine(0, tbHeight, getWidth(), tbHeight, paint);
        canvas.drawLine(tbHeight, 0, tbHeight, tbHeight, paint);
        canvas.drawLine(0, getHeight() - tbHeight, getWidth(), getHeight() - tbHeight, paint);
    }

    @Override
    protected void onSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        super.onSizeChanged(width, height, prevWidth, prevHeight);
        gradientPaint.setShader(new LinearGradient(0, tbHeight, 0, getHeight() - tbHeight, GRADIENT_COLOR, GRADIENT_POSITON, Shader.TileMode.CLAMP, null));
    }

    public abstract void createCenterLayout(RelativeLayout center);

    public static class CloseButton extends HoverAlphaButton {

        public static final OnClickListener CLOSE_SCREEN = v -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(null));

        public CloseButton(Context context) {
            this(context, CLOSE_SCREEN);
        }

        public CloseButton(Context context, Runnable behavior) {
            this(context, v -> behavior.run());
        }

        public CloseButton(Context context, OnClickListener behavior) {
            super(context);

            setOnClickListener(behavior);

            setBackground(new ImageDrawable(null, PublicTexture.ROOT_CLOSE_BUTTON.getTexture().get()));
        }
    }
}
