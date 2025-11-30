package com.phasetranscrystal.breacore.data.materials;

import com.phasetranscrystal.breacore.api.material.Element;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;
import com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate;

import static com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate.Brea;
import static com.phasetranscrystal.breacore.data.materials.BreaElements.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class ElementMaterials {

    public static void init() {
        BreaElements.init();

        Iron = elementBuilder("iron", Fe)
                .ingot()
                .color(0xeeeeee).secondaryColor(0x979797).iconSet(METALLIC)
                .element(Fe)
                .register();
    }

    public static MaterialBuilder<Material, BreaRegistrate> elementBuilder(String name, Element element) {
        return Brea.material(name);
    }
}
