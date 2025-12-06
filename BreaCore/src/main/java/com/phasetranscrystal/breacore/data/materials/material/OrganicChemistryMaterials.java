package com.phasetranscrystal.breacore.data.materials.material;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class OrganicChemistryMaterials {

    public static void register() {
        Sugar = new MaterialBuilder(BreaUtil.byPath("sugar"))
                .gem(1)
                .color(0xFFFFFF).secondaryColor(0x545468).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 12, Oxygen, 6)
                .buildAndRegister();
    }
}
