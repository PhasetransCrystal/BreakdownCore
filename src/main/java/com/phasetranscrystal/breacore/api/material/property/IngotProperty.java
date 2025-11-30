package com.phasetranscrystal.breacore.api.material.property;

public class IngotProperty implements IMaterialProperty {

    @Override
    public void verifyProperty(MaterialProperties properties) {
        properties.ensureSet(PropertyKey.DUST, true);
        if (properties.hasProperty(PropertyKey.GEM)) {
            throw new IllegalStateException(
                    "Material " + properties.getMaterial() +
                            " has both Ingot and Gem Property, which is not allowed!");
        }
    }
}
