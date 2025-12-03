package com.phasetranscrystal.breacore.api.material.property;

import com.phasetranscrystal.breacore.api.material.info.MaterialFlags;

/**
 * 聚合物信息
 */
public class PolymerProperty implements IMaterialProperty {

    @Override
    public void verifyProperty(MaterialProperties properties) {
        properties.ensureSet(PropertyKey.DUST, true);
        properties.ensureSet(PropertyKey.INGOT, true);

        properties.getMaterial().addFlags(MaterialFlags.FLAMMABLE, MaterialFlags.NO_SMASHING,
                MaterialFlags.DISABLE_DECOMPOSITION);
    }
}
