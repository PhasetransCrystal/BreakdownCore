package com.phasetranscrystal.breacore.api.blockentity.debug;

import com.phasetranscrystal.brealib.mui.TestingFragment;
import com.phasetranscrystal.brealib.mui.factory.BlockEntityUIFactory;
import com.phasetranscrystal.brealib.mui.modular.IUIHolder;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
        return new ModularUI(new TestingFragment(), this, entityPlayer);
    }
}
