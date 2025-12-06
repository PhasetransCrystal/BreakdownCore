package com.phasetranscrystal.breacore.api.item;

import com.phasetranscrystal.breacore.api.block.IMachineBlock;
import com.phasetranscrystal.breacore.api.machine.MachineDefinition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MetaMachineItem extends BlockItem {

    public MetaMachineItem(IMachineBlock block, Properties properties) {
        super(block.self(), properties);
    }

    public MachineDefinition getDefinition() {
        return ((IMachineBlock) getBlock()).getDefinition();
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();

        boolean superVal = super.placeBlock(context, state);

        if (!level.isClientSide) {
            BlockPos possiblePipe = pos.offset(side.getOpposite().getUnitVec3i());
            Block block = level.getBlockState(possiblePipe).getBlock();
        }
        return superVal;
    }
}
