package com.phasetranscrystal.breacore.api.block.debug;

import com.phasetranscrystal.breacore.api.blockentity.debug.TestBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

import static com.phasetranscrystal.breacore.data.blocks.BreaBlocks.TestMuiBlock;

public class MuiTestBlock extends Block implements EntityBlock {

    public MuiTestBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof TestBlockEntity tbe) {
            tbe.use(player);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return TestMuiBlock.getSibling(Registries.BLOCK_ENTITY_TYPE).value().create(blockPos, blockState);
    }
}
