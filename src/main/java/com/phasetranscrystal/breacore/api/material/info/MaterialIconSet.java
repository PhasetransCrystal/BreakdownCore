package com.phasetranscrystal.breacore.api.material.info;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MaterialIconSet {

    public static final Map<String, MaterialIconSet> ICON_SETS = new HashMap<>();

    public static final MaterialIconSet DULL = new MaterialIconSet("dull", null, true);

    // Implementation -----------------------------------------------------------------------------------------------

    private static int idCounter = 0;
    public final String name;
    public final int id;
    public final boolean isRootIconset;

    /**
     * This can be null if {@link MaterialIconSet#isRootIconset} is true,
     * otherwise it will be Nonnull
     */
    public final MaterialIconSet parentIconset;

    /**
     * Create a new MaterialIconSet whose parent is {@link MaterialIconSet#DULL}
     *
     * @param name the name of the iconset
     */
    public MaterialIconSet(@NotNull String name) {
        this(name, MaterialIconSet.DULL);
    }

    /**
     * Create a new MaterialIconSet whose parent is one of your choosing
     *
     * @param name          the name of the iconset
     * @param parentIconset the parent iconset
     */
    public MaterialIconSet(@NotNull String name, @NotNull MaterialIconSet parentIconset) {
        this(name, parentIconset, false);
    }

    /**
     * Create a new MaterialIconSet which is a root
     *
     * @param name          the name of the iconset
     * @param parentIconset the parent iconset, should be null if this should be a root iconset
     * @param isRootIconset true if this should be a root iconset, otherwise false
     */
    public MaterialIconSet(@NotNull String name, @Nullable MaterialIconSet parentIconset, boolean isRootIconset) {
        this.name = name.toLowerCase(Locale.ENGLISH);
        Preconditions.checkArgument(!ICON_SETS.containsKey(this.name),
                "MaterialIconSet " + this.name + " already registered!");
        this.id = idCounter++;
        this.isRootIconset = isRootIconset;
        this.parentIconset = parentIconset;
        ICON_SETS.put(this.name, this);
    }

    public static MaterialIconSet getByName(@NotNull String name) {
        return ICON_SETS.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String toString() {
        return name;
    }
}
