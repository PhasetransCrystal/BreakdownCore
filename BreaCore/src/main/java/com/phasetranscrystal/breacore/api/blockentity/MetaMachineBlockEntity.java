package com.phasetranscrystal.breacore.api.blockentity;

import com.phasetranscrystal.breacore.api.BreaAPI;
import com.phasetranscrystal.breacore.api.machine.IMachineBlockEntity;
import com.phasetranscrystal.breacore.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class MetaMachineBlockEntity extends BlockEntity implements IMachineBlockEntity {

    /// 机器实例
    @Getter
    public final MetaMachine metaMachine;
    /// 随机数
    private final long offset = BreaAPI.RNG.nextInt(20);

    protected MetaMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.metaMachine = getDefinition().createMetaMachine(this);
    }

    /// 创建方块实体
    public static MetaMachineBlockEntity createBlockEntity(BlockEntityType<?> type, BlockPos pos,
                                                           BlockState blockState) {
        return new MetaMachineBlockEntity(type, pos, blockState);
    }

    /// 注册方块实体回调
    public static void onBlockEntityRegister(BlockEntityType<BlockEntity> type) {}

    @Override
    public boolean triggerEvent(int id, int para) {
        if (id == 1) { // chunk re render
            if (level != null && level.isClientSide) {
                scheduleRenderUpdate();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentInput) {
        super.applyImplicitComponents(componentInput);
        metaMachine.applyImplicitComponents(new ExDataComponentInput() {

            @Override
            public @Nullable <T> T get(DataComponentType<? extends T> dataComponentType) {
                return componentInput.get(dataComponentType);
            }

            @Override
            public <T> T getOrDefault(DataComponentType<? extends T> component, T defaultValue) {
                return componentInput.getOrDefault(component, defaultValue);
            }
        });
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        metaMachine.collectImplicitComponents(components);
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        metaMachine.onUnload();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        metaMachine.onLoad();
    }

    @Override
    public void setChanged() {
        if (getLevel() != null) {
            getLevel().blockEntityChanged(getBlockPos());
        }
    }

    public interface ExDataComponentInput extends DataComponentGetter {}
}
