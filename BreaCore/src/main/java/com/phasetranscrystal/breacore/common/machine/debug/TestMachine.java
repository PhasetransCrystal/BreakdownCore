package com.phasetranscrystal.breacore.common.machine.debug;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.mui.MuiHelper;
import com.phasetranscrystal.brealib.mui.PublicTexture;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;
import com.phasetranscrystal.brealib.mui.preset.NarrowPaginationAEFView;
import com.phasetranscrystal.brealib.mui.widget.Widget;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;
import com.phasetranscrystal.brealib.mui.widget.custom.PlayerInventoryWidget;

import com.phasetranscrystal.breacore.api.machine.IMachineBlockEntity;
import com.phasetranscrystal.breacore.api.machine.MetaMachine;
import com.phasetranscrystal.breacore.api.machine.feature.IUIMachine;

import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ImageView;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Range;

public class TestMachine extends MetaMachine implements IUIMachine {

    public TestMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    private long tick = 0;

    @Override
    public void onLoad() {
        super.onLoad();
        var a = subscribeServerTick(this::someUpdate);
    }

    private void someUpdate() {
        tick++;
        if (tick > 20 * 5) {
            var pos = getPos();
            var y1 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
            getHolder().level().setBlockAndUpdate(y1, Blocks.SAND.defaultBlockState());
        }
        if (tick > 20 * 10) {
            var pos = getPos();
            var y1 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
            getHolder().level().setBlockAndUpdate(y1, Blocks.AIR.defaultBlockState());
            tick = 0;
        }
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(new WidgetGroup().setDynamicSized(true), this, entityPlayer)
                .widget(new Widget(800, 400) {

                    @Override
                    public void modifyView(ViewGroup container) {
                        View centerBox = new NarrowPaginationAEFView(container.getContext(), ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "ui_public/icon/test.png"), "Testing Mac") {

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

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(container.dp(getSizeWidth()), container.dp(getSizeHeight()));
                        params.gravity = Gravity.CENTER;

                        container.addView(centerBox, params);
                    }
                })
                .widget(new PlayerInventoryWidget(130, 200));
    }
}
