package com.phasetranscrystal.breacore.common.registry;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class BreaRegistration {

    public static final BreaRegistrate REGISTRATE = BreaRegistrate.create(BreaCore.MOD_ID);

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private BreaRegistration() {}
}
