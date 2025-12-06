package com.phasetranscrystal.breacore.api.machine.feature;

import com.phasetranscrystal.breacore.api.machine.MetaMachine;

public interface IMachineFeature {

    default MetaMachine self() {
        return (MetaMachine) this;
    }
}
