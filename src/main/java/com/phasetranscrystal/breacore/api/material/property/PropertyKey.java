package com.phasetranscrystal.breacore.api.material.property;

public class PropertyKey<T extends IMaterialProperty> {

    public static final PropertyKey<DustProperty> DUST = new PropertyKey<>("dust", DustProperty.class);
    public static final PropertyKey<IngotProperty> INGOT = new PropertyKey<>("ingot", IngotProperty.class);
    public static final PropertyKey<ToolProperty> TOOL = new PropertyKey<>("tool", ToolProperty.class);
    public static final PropertyKey<WoodProperty> WOOD = new PropertyKey<>("wood", WoodProperty.class);
    public static final PropertyKey<GemProperty> GEM = new PropertyKey<>("gem", GemProperty.class);
    public static final PropertyKey<FluidProperty> FLUID = new PropertyKey<>("fluid", FluidProperty.class);
    // Empty property used to allow property-less Materials without removing base type enforcement
    public static final PropertyKey<EmptyProperty> EMPTY = new PropertyKey<>("empty", EmptyProperty.class);

    private final String key;
    private final Class<T> type;

    public PropertyKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    protected String getKey() {
        return key;
    }

    protected T constructDefault() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public T cast(IMaterialProperty property) {
        return this.type.cast(property);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PropertyKey) {
            return ((PropertyKey<?>) o).getKey().equals(key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key;
    }

    public static class EmptyProperty implements IMaterialProperty {

        private EmptyProperty() {}

        @Override
        public void verifyProperty(MaterialProperties properties) {
            // no-op
        }
    }
}
