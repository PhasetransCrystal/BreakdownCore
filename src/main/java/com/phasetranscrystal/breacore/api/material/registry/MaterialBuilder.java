package com.phasetranscrystal.breacore.api.material.registry;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.phasetranscrystal.breacore.api.material.Element;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.MaterialStack;
import com.phasetranscrystal.breacore.api.material.info.MaterialFlag;
import com.phasetranscrystal.breacore.api.material.info.MaterialFlags;
import com.phasetranscrystal.breacore.api.material.info.MaterialIconSet;
import com.phasetranscrystal.breacore.api.material.property.*;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.api.tag.TagPrefix;
import com.phasetranscrystal.breacore.data.materials.BreaMaterials;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MaterialBuilder<T extends Material, P> extends AbstractBuilder<Material, T, P, MaterialBuilder<T, P>> {

    @FunctionalInterface
    public interface MaterialFactory<T extends Material> {

        T create(Material.MaterialInfo materialInfo, MaterialProperties properties, MaterialFlags flags);
    }

    public static <T extends Material, P> MaterialBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, MaterialFactory<T> factory) {
        return new MaterialBuilder<>(owner, parent, name, callback, factory);
    }

    protected MaterialBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, MaterialFactory<T> factory) {
        super(owner, parent, name, callback, BreaRegistries.MATERIAL_KEY);
        if (name.charAt(name.length() - 1) == '_')
            throw new IllegalArgumentException("Material name cannot end with a '_'!");
        materialFactory = factory;
        materialInfo = new Material.MaterialInfo(ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName()));
        properties = new MaterialProperties();
        flags = new MaterialFlags();
    }

    private final MaterialFactory<T> materialFactory;

    private final Material.MaterialInfo materialInfo;
    private final MaterialProperties properties;
    private final MaterialFlags flags;

    // TODO:
    private Set<TagPrefix> ignoredTagPrefixes = null;
    private String formula = null;
    private List<MaterialStack> composition = new ArrayList<>();
    private boolean averageRGB = false;
    // TODO:

    // TODO:添加特定Property

    /**
     * 为此材料添加{@link DustProperty}。<br/>
     * 将默认设置挖掘等级为2，且无燃烧时间（熔炉燃料）。
     *
     * @throws IllegalArgumentException 若此材料已添加{@link DustProperty}
     */
    public MaterialBuilder<T, P> dust() {
        properties.ensureSet(PropertyKey.DUST);
        return this;
    }

    /**
     * 为此材料添加{@link DustProperty}。<br/>
     * 将默认设置无燃烧时间（熔炉燃料）。
     *
     * @param harvestLevel 挖掘等级<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级
     * @throws IllegalArgumentException 若此材料已添加{@link DustProperty}
     */
    public MaterialBuilder<T, P> dust(int harvestLevel) {
        return dust(harvestLevel, 0);
    }

    /**
     * 为此材料添加{@link DustProperty}
     *
     * @param harvestLevel 挖掘等级<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级
     * @param burnTime     作为熔炉燃料时的燃烧时间（刻）
     * @throws IllegalArgumentException 若此材料已添加{@link DustProperty}
     */
    public MaterialBuilder<T, P> dust(int harvestLevel, int burnTime) {
        properties.setProperty(PropertyKey.DUST, new DustProperty(harvestLevel, burnTime));
        return this;
    }

    /**
     * 为此材料添加{@link WoodProperty}。<br/>
     * 用于将材料标记为木材，以获得各种额外的特性行为。<br/>
     * 将默认设置挖掘等级为0，燃烧时间为300（熔炉燃料）。
     *
     * @throws IllegalArgumentException 若此材料已添加{@link DustProperty}
     */
    public MaterialBuilder<T, P> wood() {
        return wood(0, 300);
    }

    /**
     * 为此材料添加{@link WoodProperty}。<br/>
     * 用于将材料标记为木材，以获得各种额外的特性行为。<br/>
     * 将默认设置燃烧时间为300（熔炉燃料）。
     *
     * @param harvestLevel 挖掘等级<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级
     * @throws IllegalArgumentException 若此材料已添加{@link DustProperty}
     */
    public MaterialBuilder<T, P> wood(int harvestLevel) {
        return wood(harvestLevel, 300);
    }

    /**
     * 为此材料添加{@link WoodProperty}。<br/>
     * 用于将材料标记为木材，以获得各种额外的特性行为。
     *
     * @param harvestLevel 挖掘等级<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级
     * @param burnTime     作为熔炉燃料时的燃烧时间（刻）
     * @throws IllegalArgumentException 若此材料已添加{@link DustProperty}
     */
    public MaterialBuilder<T, P> wood(int harvestLevel, int burnTime) {
        properties.setProperty(PropertyKey.DUST, new DustProperty(harvestLevel, burnTime));
        properties.ensureSet(PropertyKey.WOOD);
        return this;
    }

    /**
     * 为此材料添加{@link IngotProperty}。<br/>
     * 将默认设置挖掘等级为2且无燃烧时间（熔炉燃料）<br/>
     * 若当前材料未配置{@link DustProperty}，将自动添加
     *
     * @throws IllegalArgumentException 若此材料已添加{@link IngotProperty}
     */
    public MaterialBuilder<T, P> ingot() {
        properties.ensureSet(PropertyKey.INGOT);
        return this;
    }

    /**
     * 为此材料添加{@link IngotProperty}。<br/>
     * 将默认设置无燃烧时间（熔炉燃料）<br/>
     * 若当前材料未配置{@link DustProperty}，将自动添加
     *
     * @param harvestLevel 挖掘等级（设置为2将需要铁制工具）<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级（实际值-1）。
     *                     例如设置为2可使工具能够挖掘钻石矿<br/>
     *                     若已定义过挖掘等级，将被覆盖
     * @throws IllegalArgumentException 若此材料已添加{@link IngotProperty}
     */
    public MaterialBuilder<T, P> ingot(int harvestLevel) {
        return ingot(harvestLevel, 0);
    }

    /**
     * 为此材料添加{@link IngotProperty}<br/>
     * 若当前材料未配置{@link DustProperty}，将自动添加
     *
     * @param harvestLevel 挖掘等级（设置为2将需要铁制工具）<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级（实际值-1）。
     *                     例如设置为2可使工具能够挖掘钻石矿<br/>
     *                     若已定义过挖掘等级，将被覆盖
     * @param burnTime     作为熔炉燃料时的燃烧时间（刻）<br/>
     *                     若已定义过燃烧时间，将被覆盖
     * @throws IllegalArgumentException 若此材料已添加{@link IngotProperty}
     */
    public MaterialBuilder<T, P> ingot(int harvestLevel, int burnTime) {
        DustProperty prop = properties.getProperty(PropertyKey.DUST);
        if (prop == null) dust(harvestLevel, burnTime);
        else {
            if (prop.getHarvestLevel() == 2) prop.setHarvestLevel(harvestLevel);
            if (prop.getBurnTime() == 0) prop.setBurnTime(burnTime);
        }
        properties.ensureSet(PropertyKey.INGOT);
        return this;
    }

    /**
     * 为此材料添加{@link GemProperty}。<br/>
     * 将默认设置挖掘等级为2，且无燃烧时间（熔炉燃料）。<br/>
     * 若当前材料未配置{@link DustProperty}，将自动添加。
     *
     * @throws IllegalArgumentException 若此材料已添加{@link GemProperty}
     */
    public MaterialBuilder<T, P> gem() {
        properties.ensureSet(PropertyKey.GEM);
        return this;
    }

    /**
     * 为此材料添加{@link GemProperty}。<br/>
     * 将默认设置无燃烧时间（熔炉燃料）。<br/>
     * 若当前材料未配置{@link DustProperty}，将自动添加。
     *
     * @param harvestLevel 挖掘等级<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级。<br/>
     *                     若已定义过挖掘等级，将被覆盖。
     * @throws IllegalArgumentException 若此材料已添加{@link GemProperty}
     */
    public MaterialBuilder<T, P> gem(int harvestLevel) {
        return gem(harvestLevel, 0);
    }

    /**
     * 为此材料添加{@link GemProperty}。<br/>
     * 若当前材料未配置{@link DustProperty}，将自动添加。
     *
     * @param harvestLevel 挖掘等级<br/>
     *                     若此材料同时具有{@link ToolProperty}，该值也将用于决定工具的挖掘等级。<br/>
     *                     若已定义过挖掘等级，将被覆盖。
     * @param burnTime     作为熔炉燃料时的燃烧时间（刻）<br/>
     *                     若已定义过燃烧时间，将被覆盖。
     */
    public MaterialBuilder<T, P> gem(int harvestLevel, int burnTime) {
        DustProperty prop = properties.getProperty(PropertyKey.DUST);
        if (prop == null) dust(harvestLevel, burnTime);
        else {
            if (prop.getHarvestLevel() == 2) prop.setHarvestLevel(harvestLevel);
            if (prop.getBurnTime() == 0) prop.setBurnTime(burnTime);
        }
        properties.ensureSet(PropertyKey.GEM);
        return this;
    }

    public MaterialBuilder<T, P> burnTime(int burnTime) {
        DustProperty prop = properties.getProperty(PropertyKey.DUST);
        if (prop == null) {
            dust();
            prop = properties.getProperty(PropertyKey.DUST);
        }
        prop.setBurnTime(burnTime);
        return this;
    }

    /**
     * 设置此材料的颜色。<br/>
     * 默认值为0xFFFFFF，除非调用了{@link MaterialBuilder#colorAverage()}，
     * 此时将为材料成分的加权平均值。
     *
     * @param color RGB格式的颜色值
     */
    public MaterialBuilder<T, P> color(int color) {
        color(color, true);
        return this;
    }

    /**
     * 设置此材料的颜色。<br/>
     * 默认值为0xFFFFFF，除非调用了{@link MaterialBuilder#colorAverage()}，
     * 此时将为材料成分的加权平均值。
     *
     * @param color         RGB格式的颜色值
     * @param hasFluidColor 流体是否应着色
     */
    public MaterialBuilder<T, P> color(int color, boolean hasFluidColor) {
        this.materialInfo.getColors().set(0, color);
        this.materialInfo.setHasFluidColor(hasFluidColor);
        return this;
    }

    /**
     * 设置此材料的次要颜色。<br/>
     * 默认值为0xFFFFFF，除非调用了{@link MaterialBuilder#colorAverage()}，
     * 此时将为材料成分的加权平均值。
     *
     * @param color RGB格式的颜色值
     */
    public MaterialBuilder<T, P> secondaryColor(int color) {
        this.materialInfo.getColors().set(1, color);
        return this;
    }

    /**
     * 启用颜色平均值计算
     */
    public MaterialBuilder<T, P> colorAverage() {
        this.averageRGB = true;
        return this;
    }

    /**
     * 设置此材料的{@link MaterialIconSet}（图标集）。<br/>
     * 默认值根据材料具有的属性而定：<br/>
     * <ul>
     * <li>若具有{@link GemProperty}，默认值为{@link MaterialIconSet#GEM_VERTICAL}
     * <li>若具有{@link IngotProperty}或{@link DustProperty}，默认值为{@link MaterialIconSet#DULL}
     * <li>若具有{@link FluidProperty}，默认值为{@link MaterialIconSet#FLUID}
     * </ul>
     * 除非特别指定，否则将按此顺序根据第一个找到的属性确定默认值。
     *
     * @param iconSet 此材料的{@link MaterialIconSet}
     */
    public MaterialBuilder<T, P> iconSet(MaterialIconSet iconSet) {
        materialInfo.setIconSet(iconSet);
        return this;
    }

    /**
     * 设置材料的成分
     *
     * @param components 成分数组，格式为[材料1, 数量1, 材料2, 数量2, ...]
     */
    public MaterialBuilder<T, P> components(Object... components) {
        Preconditions.checkArgument(
                components.length % 2 == 0,
                "材料成分列表格式错误！");

        for (int i = 0; i < components.length; i += 2) {
            if (components[i] == null) {
                throw new IllegalArgumentException(
                        "材料成分列表中的材料为null，材料：" + this.materialInfo.getResourceLocation());
            }
            composition.add(new MaterialStack(
                    components[i] instanceof CharSequence chars ? BreaMaterials.get(chars.toString()) :
                            (Material) components[i],
                    ((Number) components[i + 1]).longValue()));
        }
        return this;
    }

    /**
     * 设置材料的成分堆栈
     *
     * @param components 材料堆栈数组
     */
    public MaterialBuilder<T, P> componentStacks(MaterialStack... components) {
        composition = Arrays.asList(components);
        return this;
    }

    /**
     * 设置材料的成分堆栈
     *
     * @param components 不可变的材料堆栈列表
     */
    public MaterialBuilder<T, P> componentStacks(ImmutableList<MaterialStack> components) {
        composition = components;
        return this;
    }

    /**
     * 向此材料添加{@link MaterialFlags}（材料标志）。<br/>
     */
    public MaterialBuilder<T, P> flags(MaterialFlag... flags) {
        this.flags.addFlags(flags);
        return this;
    }

    /**
     * 添加要忽略的{@link TagPrefix}（标签前缀）。<br/>
     */
    public MaterialBuilder<T, P> ignoredTagPrefixes(TagPrefix... prefixes) {
        if (this.ignoredTagPrefixes == null) {
            this.ignoredTagPrefixes = new HashSet<>();
        }
        this.ignoredTagPrefixes.addAll(Arrays.asList(prefixes));
        return this;
    }

    /**
     * 设置材料的元素
     *
     * @param element 元素
     */
    public MaterialBuilder<T, P> element(Element element) {
        this.materialInfo.setElement(element);
        return this;
    }

    /**
     * 设置材料的化学式
     *
     * @param formula 化学式
     */
    public MaterialBuilder<T, P> formula(String formula) {
        this.formula = formula;
        return this;
    }

    public MaterialBuilder<T, P> toolStats(ToolProperty toolProperty) {
        properties.setProperty(PropertyKey.TOOL, toolProperty);
        return this;
    }

    private T toRegister;

    @Override
    protected @NonnullType @NotNull T createEntry() {
        if (toRegister == null) {
            materialInfo.setComponentList(ImmutableList.copyOf(composition));
            toRegister = materialFactory.create(materialInfo, properties, flags);
            if (formula != null) {
                toRegister.setFormula(formula);
            }
            materialInfo.verifyInfo(properties, averageRGB);
        }
        return toRegister;
    }

    @Override
    public @NotNull MaterialEntry<T> register() {
        return (MaterialEntry<T>) super.register();
    }

    @Override
    protected @NotNull MaterialEntry<T> createEntryWrapper(@NotNull DeferredHolder<Material, T> delegate) {
        return new MaterialEntry<>(getOwner(), delegate);
    }
}
