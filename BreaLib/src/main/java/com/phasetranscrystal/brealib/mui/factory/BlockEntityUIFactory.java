package com.phasetranscrystal.brealib.mui.factory;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.mui.modular.IUIHolder;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityUIFactory extends UIFactory<BlockEntity> {

    public static final BlockEntityUIFactory INSTANCE = new BlockEntityUIFactory();

    private BlockEntityUIFactory() {
        super(ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "block_entity"));
    }

    @Override
    protected ModularUI createUITemplate(BlockEntity holder, Player entityPlayer) {
        if (holder instanceof IUIHolder) {
            return ((IUIHolder) holder).createUI(entityPlayer);
        }
        return null;
    }

    @Override
    protected BlockEntity readHolderFromSyncData(RegistryFriendlyByteBuf syncData) {
        Level world = Minecraft.getInstance().level;
        return world == null ? null : world.getBlockEntity(syncData.readBlockPos());
    }

    @Override
    protected void writeHolderToSyncData(RegistryFriendlyByteBuf syncData, BlockEntity holder) {
        syncData.writeBlockPos(holder.getBlockPos());
    }
}
