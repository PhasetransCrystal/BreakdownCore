package com.phasetranscrystal.breacore.common.machine.debug;

import com.phasetranscrystal.brealib.mui.TestingFragment;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;

import com.phasetranscrystal.breacore.api.machine.IMachineBlockEntity;
import com.phasetranscrystal.breacore.api.machine.MetaMachine;
import com.phasetranscrystal.breacore.api.machine.feature.IUIMachine;

import net.minecraft.world.entity.player.Player;

public class TestMachine extends MetaMachine implements IUIMachine {

    public TestMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(new TestingFragment(), this, entityPlayer);
    }
}
