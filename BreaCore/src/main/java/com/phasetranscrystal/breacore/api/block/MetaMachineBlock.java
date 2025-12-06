package com.phasetranscrystal.breacore.api.block;

import com.phasetranscrystal.brealib.utils.RotationState;

import com.phasetranscrystal.breacore.api.machine.IMachineBlockEntity;
import com.phasetranscrystal.breacore.api.machine.MachineDefinition;
import com.phasetranscrystal.breacore.api.machine.MetaMachine;
import com.phasetranscrystal.breacore.api.machine.feature.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.InteractionResult.*;

public class MetaMachineBlock extends AppearanceBlock implements IMachineBlock {

    @Getter
    public final MachineDefinition definition;
    @Getter
    public final RotationState rotationState;

    public MetaMachineBlock(Properties properties, MachineDefinition definition) {
        super(properties);
        this.definition = definition;
        this.rotationState = RotationState.get();
        if (rotationState != RotationState.NONE) {
            BlockState defaultState = this.defaultBlockState().setValue(rotationState.property,
                    rotationState.defaultDirection);
            if (definition.isAllowExtendedFacing()) {
                defaultState = defaultState.setValue(IMachineBlock.UPWARDS_FACING_PROPERTY, Direction.NORTH);
            }
            registerDefaultState(defaultState);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockProperties.SERVER_TICK);
        RotationState rotationState = RotationState.get();
        if (rotationState != RotationState.NONE) {
            pBuilder.add(rotationState.property);
            if (MachineDefinition.getBuilt().isAllowExtendedFacing()) {
                pBuilder.add(IMachineBlock.UPWARDS_FACING_PROPERTY);
            }
        }
    }

    @Nullable
    public MetaMachine getMachine(BlockGetter level, BlockPos pos) {
        return MetaMachine.getMachine(level, pos);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getRotationState() == RotationState.NONE ? definition.getShape(Direction.NORTH) :
                definition.getShape(pState.getValue(getRotationState().property));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        var machine = getMachine(level, pos);
        if (machine != null) {
            machine.animateTick(random);
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState state, @Nullable LivingEntity placer, ItemStack pStack) {
        if (!pLevel.isClientSide) {
            var machine = getMachine(pLevel, pPos);
            if (machine != null) {
                if (placer instanceof ServerPlayer sPlayer) {
                    machine.setOwnerUUID(sPlayer.getUUID());
                    machine.markDirty();
                }
            }
            if (machine instanceof IDropSaveMachine dropSaveMachine) {
                CustomData tag = pStack.get(DataComponents.BLOCK_ENTITY_DATA);
                if (tag != null) {
                    dropSaveMachine.loadFromItem(tag.copyTag());
                }
            }
            if (machine instanceof IMachineLife machineLife) {
                machineLife.onMachinePlaced(placer, pStack);
            }
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        // needed to trigger block updates so machines connect to open cables properly.
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        RotationState rotationState = getRotationState();
        var player = context.getPlayer();
        var blockPos = context.getClickedPos();
        var state = defaultBlockState();
        if (player != null && rotationState != RotationState.NONE) {
            if (rotationState == RotationState.Y_AXIS) {
                state = state.setValue(rotationState.property, Direction.UP);
            } else {
                state = state.setValue(rotationState.property, player.getDirection().getOpposite());
            }
            Vec3 pos = player.position();
            if (Math.abs(pos.x - (double) ((float) blockPos.getX() + 0.5F)) < 2.0D &&
                    Math.abs(pos.z - (double) ((float) blockPos.getZ() + 0.5F)) < 2.0D) {
                double d0 = pos.y + (double) player.getEyeHeight();
                if (d0 - (double) blockPos.getY() > 2.0D && rotationState.test(Direction.UP)) {
                    state = state.setValue(rotationState.property, Direction.UP);
                }
                if ((double) blockPos.getY() - d0 > 0.0D && rotationState.test(Direction.DOWN)) {
                    state = state.setValue(rotationState.property, Direction.DOWN);
                }
            }
            if (getDefinition().isAllowExtendedFacing()) {
                Direction frontFacing = state.getValue(rotationState.property);
                if (frontFacing == Direction.UP) {
                    state = state.setValue(IMachineBlock.UPWARDS_FACING_PROPERTY, player.getDirection());
                } else if (frontFacing == Direction.DOWN) {
                    state = state.setValue(IMachineBlock.UPWARDS_FACING_PROPERTY, player.getDirection().getOpposite());
                }
            }
        }
        return state;
    }

    public Direction getFrontFacing(BlockState state) {
        return getRotationState() == RotationState.NONE ? Direction.NORTH : state.getValue(getRotationState().property);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack itemStack = super.getCloneItemStack(level, pos, state, includeData, player);
        if (getMachine(level, pos) instanceof IDropSaveMachine dropSaveMachine && dropSaveMachine.savePickClone()) {
            CompoundTag tag = itemStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
            // TODO remove in future version.
            // dropSaveMachine.saveToItem(tag);
            itemStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
        }
        return itemStack;
    }
    /*
     * @Override
     * public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext level, List<Component> tooltip,
     * TooltipFlag flag) {
     * definition.getTooltipBuilder().accept(stack, tooltip);
     * String mainKey = String.format("%s.machine.%s.tooltip", definition.getId().getNamespace(),
     * definition.getId().getPath());
     * if (LocalizationUtils.exist(mainKey)) {
     * tooltip.add(1, Component.translatable(mainKey));
     * }
     * }
     */

    @Override
    protected boolean triggerEvent(BlockState state, Level pLevel, BlockPos pPos, int pId, int pParam) {
        BlockEntity tile = pLevel.getBlockEntity(pPos);
        if (tile != null) {
            return tile.triggerEvent(pId, pParam);
        }
        return false;
    }

    @Override
    protected BlockState rotate(BlockState pState, Rotation pRotation) {
        if (this.rotationState == RotationState.NONE) {
            return pState;
        }
        return pState.setValue(this.rotationState.property,
                pRotation.rotate(pState.getValue(this.rotationState.property)));
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity tileEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        var drops = super.getDrops(state, builder);
        if (tileEntity instanceof IMachineBlockEntity holder) {
            var machine = holder.getMetaMachine();
            if (machine instanceof IMachineModifyDrops machineModifyDrops) {
                machineModifyDrops.onDrops(drops);
            }
        }
        return drops;
    }
    /*
     * @Override
     * public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
     * if (pState.hasBlockEntity()) {
     * if (!pState.is(pNewState.getBlock())) { // new block
     * MetaMachine machine = getMachine(pLevel, pPos);
     * if (machine instanceof IMachineLife machineLife) {
     * machineLife.onMachineRemoved();
     * }
     * if (machine != null) {
     * for (Direction direction : BreaUtil.DIRECTIONS) {
     * machine.getCoverContainer().removeCover(direction, null);
     * }
     * }
     * 
     * pLevel.updateNeighbourForOutputSignal(pPos, this);
     * pLevel.removeBlockEntity(pPos);
     * } else if (rotationState != RotationState.NONE) { // old block different facing
     * var oldFacing = pState.getValue(rotationState.property);
     * var newFacing = pNewState.getValue(rotationState.property);
     * if (newFacing != oldFacing) {
     * var machine = getMachine(pLevel, pPos);
     * if (machine != null) {
     * machine.onRotated(oldFacing, newFacing);
     * }
     * }
     * }
     * }
     * }
     */

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var machine = getMachine(level, pos);
        boolean shouldOpenUi = true;

        if (machine instanceof IInteractedMachine interactedMachine) {
            var result = interactedMachine.onUseWithItem(stack, state, level, pos, player, hand, hit);
            if (result != InteractionResult.PASS) return result;
        }
        if (shouldOpenUi && machine instanceof IUIMachine uiMachine) {
            return uiMachine.tryToOpenUI(player, hand, hit);
        }
        return shouldOpenUi ? InteractionResult.PASS : InteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        var machine = getMachine(level, pos);
        if (machine instanceof IUIMachine uiMachine) {
            return uiMachine.tryToOpenUI(player, InteractionHand.MAIN_HAND, hit);
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    public boolean canConnectRedstone(BlockGetter level, BlockPos pos, @Nullable Direction side) {
        return getMachine(level, pos).canConnectRedstone(side);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getMachine(level, pos).getOutputSignal(direction);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getMachine(level, pos).getOutputDirectSignal(direction);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getMachine(level, pos).getAnalogOutputSignal();
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation fromPos,
                                   boolean isMoving) {
        var machine = getMachine(level, pos);
        if (machine != null) {
            machine.onNeighborChanged(block, fromPos, isMoving);
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Nullable
    @Override
    public BlockState getBlockAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                         @Nullable BlockState sourceState, BlockPos sourcePos) {
        var machine = getMachine(level, pos);
        if (machine != null) {
            return machine.getBlockAppearance(state, level, pos, side, sourceState, sourcePos);
        }
        return super.getBlockAppearance(state, level, pos, side, sourceState, sourcePos);
    }
}
