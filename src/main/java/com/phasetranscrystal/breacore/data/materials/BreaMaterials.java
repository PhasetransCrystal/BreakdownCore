package com.phasetranscrystal.breacore.data.materials;

import net.neoforged.neoforge.registries.DeferredHolder;

import com.phasetranscrystal.breacore.api.material.MarkerMaterial;
import com.phasetranscrystal.breacore.api.material.MarkerMaterials;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.registry.MaterialEntry;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate.Brea;

public class BreaMaterials {

    public static final MarkerMaterial NULL = new MarkerMaterial(BreaUtil.byPath("null"));
    public static MaterialEntry<Material> Iron;

    public static void init() {
        MarkerMaterials.init();
        BreaMaterialIconSet.init();
        ElementMaterials.init();
    }

    @NotNull
    public static Material get(String name) {
        var entry = Brea.get(name, BreaRegistries.MATERIAL_KEY);
        var mat = Optional.of(entry).map(DeferredHolder::get);
        return mat.orElse(BreaMaterials.NULL);
    }
}
