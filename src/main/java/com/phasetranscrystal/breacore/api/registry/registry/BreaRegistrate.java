package com.phasetranscrystal.breacore.api.registry.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;
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

    public MaterialBuilder<Material, BreaRegistrate> material(String name) {
        return material(self(), name);
    }

    public <P> MaterialBuilder<Material, P> material(P owner, String name) {
        return material(owner, name, Material::new);
    }

    public <T extends Material> MaterialBuilder<T, BreaRegistrate> material(String name, MaterialBuilder.MaterialFactory<T> factory) {
        return material(self(), name, factory);
    }

    public <T extends Material, P> MaterialBuilder<T, P> material(P owner, String name, MaterialBuilder.MaterialFactory<T> factory) {
        return entry(name, callback -> MaterialBuilder.create(self(), owner, name, callback, factory));
    }
}
