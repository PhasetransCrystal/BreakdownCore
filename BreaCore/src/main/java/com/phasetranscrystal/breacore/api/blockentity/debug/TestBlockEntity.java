package com.phasetranscrystal.breacore.api.blockentity.debug;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.mui.MuiHelper;
import com.phasetranscrystal.brealib.mui.PublicTexture;
import com.phasetranscrystal.brealib.mui.factory.BlockEntityUIFactory;
import com.phasetranscrystal.brealib.mui.modular.IUIHolder;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;
import com.phasetranscrystal.brealib.mui.preset.NarrowPaginationAEFView;
import com.phasetranscrystal.brealib.mui.preset.view.SimpleItemView;
import com.phasetranscrystal.brealib.mui.widget.Widget;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;

import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ImageView;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Range;

public class TestBlockEntity extends BlockEntity implements IUIHolder.Block {

    public TestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void use(Player player) {
        if (getLevel().isClientSide) {
            player.displayClientMessage(Component.literal("§7<DEBUG>§r Try Open UI"), false);
            BlockEntityUIFactory.INSTANCE.openUI(this, player);
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
                                textView.setId(1300);
                                RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textViewParams.addRule(RelativeLayout.BELOW, 1200);
                                textViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                layout.addView(textView, textViewParams);

                                ItemStack stack = new ItemStack(switch (index) {
                                    case 1 -> Items.APPLE;
                                    case 2 -> Items.GOLDEN_APPLE;
                                    case 3 -> Items.ENCHANTED_GOLDEN_APPLE;
                                    default -> Items.CHEST;
                                });
                                stack.setCount(index);
                                SimpleItemView itemView = new SimpleItemView(getContext(), stack);
                                RelativeLayout.LayoutParams itemViewParams = new RelativeLayout.LayoutParams(dp(40), dp(40));
                                itemViewParams.addRule(RelativeLayout.RIGHT_OF, 1300);
                                itemViewParams.setMargins(index * dp(20), 0, 0, 0);
                                layout.addView(itemView, itemViewParams);
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
