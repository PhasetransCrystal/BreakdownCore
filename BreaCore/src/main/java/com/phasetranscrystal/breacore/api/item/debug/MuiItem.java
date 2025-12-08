package com.phasetranscrystal.breacore.api.item.debug;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.mui.MuiHelper;
import com.phasetranscrystal.brealib.mui.PublicTexture;
import com.phasetranscrystal.brealib.mui.factory.HeldItemUIFactory;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;
import com.phasetranscrystal.brealib.mui.preset.NarrowPaginationAEFView;
import com.phasetranscrystal.brealib.mui.widget.Widget;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;

import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ImageView;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class MuiItem extends Item implements HeldItemUIFactory.IHeldItemUIHolder {

    public MuiItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player.level().isClientSide) {
            player.displayClientMessage(Component.literal("§7<DEBUG>§r Try Open UI"), false);
            return HeldItemUIFactory.INSTANCE.openUI(player, hand) ?
                    InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public ModularUI createUI(Player entityPlayer, HeldItemUIFactory.HeldItemHolder holder) {
        return new ModularUI(new WidgetGroup().setDynamicSized(true), holder, entityPlayer)
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
                });
    }
}
