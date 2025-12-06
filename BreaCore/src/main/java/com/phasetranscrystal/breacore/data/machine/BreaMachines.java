package com.phasetranscrystal.breacore.data.machine;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.BreaAPI;
import com.phasetranscrystal.breacore.api.machine.MachineDefinition;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.core.Holder;

import java.util.Optional;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static com.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs.MACHINE;

public class BreaMachines {

    static {
        REGISTRATE.creativeModeTab(() -> MACHINE);
        BreaRegistries.MACHINES.unfreeze(true);
    }
    public static MachineDefinition DebugTestMachine;

    public static void init() {
        DebugMachines.init();

        BreaAPI.postRegisterEvent(BreaRegistries.MACHINES);
        BreaRegistries.MACHINES.freeze();
    }

    public static Optional<MachineDefinition> get(String name) {
        return BreaRegistries.MACHINES.get(BreaUtil.byPath(name)).map(Holder.Reference::value);
    }
}
