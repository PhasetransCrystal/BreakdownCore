package com.phasetranscrystal.breacore.api.block;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.tag.TagPrefix;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.NotNull;

public class OreBlock extends MaterialBlock {

    public OreBlock(Properties properties, TagPrefix tagPrefix, Material material, boolean registerModel) {
        super(properties, tagPrefix, material, false);
        if (registerModel && BreaUtil.isClientSide()) {
            // OreBlockRenderer.create(this);
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                        Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }
}
