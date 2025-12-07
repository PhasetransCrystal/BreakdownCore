package com.phasetranscrystal.brealib.mui;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.mui.preset.NarrowPaginationAEFView;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;

import net.minecraft.resources.ResourceLocation;

import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ImageView;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.Range;

public class TestingFragment extends WidgetGroup {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, DataSet savedInstanceState) {
        FrameLayout layout = new FrameLayout(requireContext());

        View centerBox = new NarrowPaginationAEFView(requireContext(), ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "ui_public/icon/test.png"), "Testing Mac") {

            @Override
            public @Range(from = 0, to = 4) int getPaginationCount() {
                return 3;
            }

            @Override
            public View createView(int index) {
                RelativeLayout layout = new RelativeLayout(getContext());
                MuiHelper.setTestingBoarder(layout);

                ImageView image = new ImageView(getContext());
                image.setImage(icon);
                image.setId(1200);
                MuiHelper.setTestingBoarder(image);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp(40), dp(40));
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(image, params);

                TextView textView = new TextView(getContext());
                textView.setText("index:" + index);
                textView.setTextSize(12);
                RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textViewParams.addRule(RelativeLayout.BELOW, 1200);
                textViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layout.addView(textView, textViewParams);
                return layout;
            }

            @Override
            public ResourceLocation getPaginationIcon(int index) {
                return PublicTexture.ICON_BACKPACK.getLocation();
            }
        };

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(layout.dp(800), layout.dp(400));
        params.gravity = Gravity.CENTER;
        centerBox.setLayoutParams(params);

        layout.addView(centerBox);

        return layout;
    }
}
