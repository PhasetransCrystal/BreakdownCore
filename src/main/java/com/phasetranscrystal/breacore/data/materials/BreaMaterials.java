package com.phasetranscrystal.breacore.data.materials;

import net.neoforged.neoforge.registries.DeferredHolder;

import com.phasetranscrystal.breacore.api.material.MarkerMaterial;
import com.phasetranscrystal.breacore.api.material.MarkerMaterials;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.info.MaterialFlag;
import com.phasetranscrystal.breacore.api.material.registry.MaterialEntry;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate.Brea;

public class BreaMaterials {

    public static final MarkerMaterial NULL = new MarkerMaterial(BreaUtil.byPath("null"));
    public static MaterialEntry<Material> Iron;

    public static final List<MaterialFlag> STD_METAL = new ArrayList<>();
    public static final List<MaterialFlag> EXT_METAL = new ArrayList<>();
    public static final List<MaterialFlag> EXT2_METAL = new ArrayList<>();

    static {
        STD_METAL.add(GENERATE_PLATE);

        EXT_METAL.addAll(STD_METAL);
        EXT_METAL.add(GENERATE_ROD);

        EXT2_METAL.addAll(EXT_METAL);
        EXT2_METAL.addAll(Arrays.asList(GENERATE_LONG_ROD, GENERATE_BOLT_SCREW));
    }

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
