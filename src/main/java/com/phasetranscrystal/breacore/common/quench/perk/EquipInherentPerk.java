package com.phasetranscrystal.breacore.common.quench.perk;

public abstract class EquipInherentPerk extends Perk {

    @Override
    public int getPerkWeight() {
        return 0;
    }

    @Override
    public boolean forceEnable() {
        return true;
    }
}
