package com.phasetranscrystal.breacore.common.machine.debug;

import com.phasetranscrystal.brealib.mui.TestingFragment;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;

import com.phasetranscrystal.breacore.api.machine.IMachineBlockEntity;
import com.phasetranscrystal.breacore.api.machine.MetaMachine;
import com.phasetranscrystal.breacore.api.machine.feature.IUIMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class TestMachine extends MetaMachine implements IUIMachine {

    public TestMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(new TestingFragment(), this, entityPlayer);
    }

    private long tick = 0;

    @Override
    public void onLoad() {
        super.onLoad();
        subscribeServerTick(() -> {
            tick++;
            if (tick > 20 * 5) {
                var pos = getPos();
                var y1 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                getHolder().level().setBlockAndUpdate(y1, Blocks.SAND.defaultBlockState());
            }
            if (tick > 20 * 10) {
                var pos = getPos();
                var y1 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                getHolder().level().setBlockAndUpdate(y1, Blocks.AIR.defaultBlockState());
                tick = 0;
            }
        });
    }
}
