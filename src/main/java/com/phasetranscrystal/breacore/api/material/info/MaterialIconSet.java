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
     * 父图标集。如果{@link MaterialIconSet#isRootIconset}为true，则可以为null，
     * 否则必须为非null。
     */
    public final MaterialIconSet parentIconset;

    /**
     * 创建一个新的MaterialIconSet，其父图标集为{@link MaterialIconSet#DULL}
     *
     * @param name 图标集名称
     */
    public MaterialIconSet(@NotNull String name) {
        this(name, MaterialIconSet.DULL);
    }

    /**
     * 创建一个新的MaterialIconSet，可指定父图标集
     *
     * @param name          图标集名称
     * @param parentIconset 父图标集
     */
    public MaterialIconSet(@NotNull String name, @NotNull MaterialIconSet parentIconset) {
        this(name, parentIconset, false);
    }

    /**
     * 创建一个新的MaterialIconSet，可作为根图标集
     *
     * @param name          图标集名称
     * @param parentIconset 父图标集，若此图标集为根图标集则应为null
     * @param isRootIconset 如果此图标集为根图标集则为true，否则为false
     */
    public MaterialIconSet(@NotNull String name, @Nullable MaterialIconSet parentIconset, boolean isRootIconset) {
        this.name = name.toLowerCase(Locale.ENGLISH);
        Preconditions.checkArgument(!ICON_SETS.containsKey(this.name),
                "MaterialIconSet " + this.name + " 已注册！");
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
