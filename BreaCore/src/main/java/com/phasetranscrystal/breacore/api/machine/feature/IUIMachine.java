package com.phasetranscrystal.breacore.api.machine.feature;

import com.phasetranscrystal.brealib.mui.modular.IUIHolder;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.mui.factory.MachineUIFactory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public interface IUIMachine extends IUIHolder, IMachineFeature {

    default boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return true;
    }

    default InteractionResult tryToOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.shouldOpenUI(player, hand, hit)) {
            if (player.level().isClientSide) {
                MachineUIFactory.INSTANCE.openUI(self(), player);
            }
        } else {
            return InteractionResult.PASS;
        }
        return player.level().isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    @Override
    default boolean isInvalid() {
        return self().isInValid();
    }

    @Override
    default boolean isRemote() {
        var level = self().getLevel();
        return level == null ? BreaUtil.isClientThread() : level.isClientSide;
    }

    @Override
    default void markAsDirty() {
        self().markDirty();
    }
}
