package com.phasetranscrystal.breacore.data.materials;

import static com.phasetranscrystal.breacore.api.registry.BreaRegistries.MATERIALS;

public class BreaElements {

    static {
        MATERIALS.unfreeze(true);
    }

    public static void init() {
        MATERIALS.freeze();
    }
}
