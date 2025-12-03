package com.phasetranscrystal.breacore.common.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

public class BreaRegistration {

    public static final BreaRegistrate REGISTRATE = BreaRegistrate.create(BreaCore.MOD_ID);

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private BreaRegistration() {}
}
