package com.phasetranscrystal.breacore.api.registry.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.phasetranscrystal.breacore.BreaCore;
import com.tterrag.registrate.AbstractRegistrate;
import org.jetbrains.annotations.NotNull;

public class BreaRegistrate extends AbstractRegistrate<BreaRegistrate> {

    public static BreaRegistrate Brea;

    public static @NotNull BreaRegistrate create(@NotNull String modId) {
        return new BreaRegistrate(modId);
    }

    protected BreaRegistrate(String modId) {
        super(modId);
    }

    static {
        Brea = create(BreaCore.MOD_ID);
        Brea.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }
}
