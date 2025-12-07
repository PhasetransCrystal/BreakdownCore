package com.phasetranscrystal.breacore.api.item.debug;

import com.phasetranscrystal.brealib.mui.TestingFragment;
import com.phasetranscrystal.brealib.mui.factory.HeldItemUIFactory;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

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
        return new ModularUI(new TestingFragment(), holder, entityPlayer);
    }
}
