package com.phasetranscrystal.breacore.data.machine;

import com.phasetranscrystal.brealib.utils.RotationState;

import com.phasetranscrystal.breacore.common.machine.debug.TestMachine;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static com.phasetranscrystal.breacore.data.machine.BreaMachines.*;

public class DebugMachines {

    public static void init() {
        DebugTestMachine = REGISTRATE.machine("test_machine", TestMachine::new)
                .rotationState(RotationState.ALL)
                .allowExtendedFacing(true)
                .register();
    }
}
