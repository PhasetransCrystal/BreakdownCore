package com.phasetranscrystal.breacore.api.machine.registrate;

import com.phasetranscrystal.brealib.utils.RotationState;

import com.phasetranscrystal.breacore.api.block.IMachineBlock;
import com.phasetranscrystal.breacore.api.blockentity.MetaMachineBlockEntity;
import com.phasetranscrystal.breacore.api.item.MetaMachineItem;
import com.phasetranscrystal.breacore.api.machine.IMachineBlockEntity;
import com.phasetranscrystal.breacore.api.machine.MachineDefinition;
import com.phasetranscrystal.breacore.api.machine.MetaMachine;
import com.phasetranscrystal.breacore.api.machine.mulityblock.PartAbility;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.config.ConfigHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.*;

@Accessors(chain = true, fluent = true)
public class MachineBuilder<DEFINITION extends MachineDefinition> {

    protected final AbstractRegistrate<?> registrate;
    protected final String name;
    protected final BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory;
    protected final BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory;
    protected final TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory;
    @Setter // non-final for KJS
    protected Function<ResourceLocation, DEFINITION> definition;
    @Setter // non-final for KJS
    protected Function<IMachineBlockEntity, MetaMachine> machine;
    @Setter
    private VoxelShape shape = Shapes.block();
    @Setter
    private RotationState rotationState = RotationState.NON_Y_AXIS;
    /**
     * Whether this machine can be rotated or face upwards.
     * todo: set to true by default if we manage to rotate the model accordingly
     */
    @Setter
    private boolean allowExtendedFacing = false;
    @Setter
    private boolean hasTESR;
    @Setter
    private boolean renderMultiblockWorldPreview = true;
    @Setter
    private boolean renderMultiblockXEIPreview = true;
    @Setter
    private NonNullUnaryOperator<BlockBehaviour.Properties> blockProp = p -> p;
    @Setter
    private NonNullUnaryOperator<Item.Properties> itemProp = p -> p;
    @Setter
    @Nullable
    private Consumer<BlockBuilder<? extends Block, ?>> blockBuilder;
    @Setter
    @Nullable
    private Consumer<ItemBuilder<? extends MetaMachineItem, ?>> itemBuilder;
    @Setter
    private NonNullConsumer<BlockEntityType<BlockEntity>> onBlockEntityRegister = MetaMachineBlockEntity::onBlockEntityRegister;
    @Getter
    @Setter // getter for KJS
    private int tier;
    @Setter
    private int paintingColor = Long.decode(ConfigHolder.INSTANCE.client.defaultPaintingColor).intValue();
    @Setter
    private BiFunction<ItemStack, Integer, Integer> itemColor = ((itemStack, tintIndex) -> tintIndex == 1 ? paintingColor : -1);
    private PartAbility[] abilities = new PartAbility[0];
    private final Consumer<Component> tooltips = c -> {};
    @Setter
    @Nullable
    private BiConsumer<ItemStack, Consumer<Component>> tooltipBuilder;
    @Setter
    private boolean alwaysTryModifyRecipe;
    @Getter
    @Setter
    private boolean regressWhenWaiting = true;

    @Setter
    @Nullable
    private Supplier<BlockState> appearance;
    @Getter // getter for KJS
    @Setter
    @Nullable
    private String langValue = null;

    public MachineBuilder(AbstractRegistrate<?> registrate, String name,
                          Function<ResourceLocation, DEFINITION> definition,
                          Function<IMachineBlockEntity, MetaMachine> machine,
                          BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory,
                          BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
                          TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        this.registrate = registrate;
        this.name = name;
        this.machine = machine;
        this.blockFactory = blockFactory;
        this.itemFactory = itemFactory;
        this.blockEntityFactory = blockEntityFactory;
        this.definition = definition;
    }

    public MachineBuilder<DEFINITION> appearanceBlock(Supplier<? extends Block> block) {
        appearance = () -> block.get().defaultBlockState();
        return this;
    }

    public MachineBuilder<DEFINITION> tooltips(Component... components) {
        Arrays.stream(components).forEach(tooltips);
        return this;
    }

    public MachineBuilder<DEFINITION> conditionalTooltip(Component component, Supplier<Boolean> condition) {
        return conditionalTooltip(component, condition.get());
    }

    public MachineBuilder<DEFINITION> conditionalTooltip(Component component, boolean condition) {
        if (condition)
            tooltips.accept(component);
        return this;
    }

    public MachineBuilder<DEFINITION> abilities(PartAbility... abilities) {
        this.abilities = abilities;
        return this;
    }

    public MachineBuilder<DEFINITION> multiblockPreviewRenderer(boolean multiBlockWorldPreview,
                                                                boolean multiBlockXEIPreview) {
        this.renderMultiblockWorldPreview = multiBlockWorldPreview;
        this.renderMultiblockXEIPreview = multiBlockXEIPreview;
        return this;
    }

    protected DEFINITION createDefinition() {
        return definition.apply(ResourceLocation.fromNamespaceAndPath(registrate.getModid(), name));
    }

    public @NotNull DEFINITION register() {
        var definition = createDefinition();

        var blockBuilder = BlockBuilderWrapper.makeBlockBuilder(this, definition);
        if (this.langValue != null) {
            blockBuilder.lang(langValue);
            definition.setLangValue(langValue);
        }
        if (this.blockBuilder != null) {
            this.blockBuilder.accept(blockBuilder);
        }
        var block = blockBuilder.register();

        var itemBuilder = ItemBuilderWrapper.makeItemBuilder(this, block);
        if (this.itemBuilder != null) {
            this.itemBuilder.accept(itemBuilder);
        }
        var item = itemBuilder.register();

        var blockEntityBuilder = registrate
                .blockEntity(name, (type, pos, state) -> blockEntityFactory.apply(type, pos, state).self())
                .onRegister(onBlockEntityRegister)
                .validBlock(block);
        var blockEntity = blockEntityBuilder.register();
        // definition.setRecipeTypes(recipeTypes);
        definition.setBlockSupplier(block);
        definition.setItemSupplier(item);
        definition.setTier(tier);
        // definition.setRecipeOutputLimits(recipeOutputLimits);
        definition.setBlockEntityTypeSupplier(blockEntity::get);
        definition.setMachineSupplier(machine);
        definition.setTooltipBuilder((itemStack, components) -> {
            components = components.andThen(tooltips);
            if (tooltipBuilder != null) tooltipBuilder.accept(itemStack, components);
        });
        definition.setAlwaysTryModifyRecipe(alwaysTryModifyRecipe);
        definition.setRegressWhenWaiting(this.regressWhenWaiting);
        if (appearance == null) {
            appearance = block::getDefaultState;
        }
        definition.setAppearance(appearance);
        definition.setAllowExtendedFacing(allowExtendedFacing);
        // definition.setRenderer(BreaUtil.isClientSide() ? renderer.get() : IRenderer.EMPTY);
        definition.setShape(shape);
        definition.setDefaultPaintingColor(paintingColor);
        definition.setRenderXEIPreview(renderMultiblockXEIPreview);
        definition.setRenderWorldPreview(renderMultiblockWorldPreview);
        BreaRegistries.MACHINES.register(definition.getId(), definition);
        return definition;
    }

    static class BlockBuilderWrapper {

        @SuppressWarnings("removal")
        public static <D extends MachineDefinition> BlockBuilder<Block, MachineBuilder<D>> makeBlockBuilder(MachineBuilder<D> builder,
                                                                                                            D definition) {
            return builder.registrate.block(builder, builder.name, properties -> {
                RotationState.set(builder.rotationState);
                MachineDefinition.setBuilt(definition);
                var b = builder.blockFactory.apply(properties, definition);
                RotationState.clear();
                MachineDefinition.clearBuilt();
                return b.self();
            })
                    .color(() -> () -> IMachineBlock::colorTinted)
                    .initialProperties(() -> Blocks.DISPENSER)
                    .properties(BlockBehaviour.Properties::noLootTable)
                    .blockstate(NonNullBiConsumer::noop)
                    .properties(builder.blockProp)
                    .onRegister(b -> Arrays.stream(builder.abilities).forEach(a -> a.register(builder.tier, b)));
        }
    }

    static class ItemBuilderWrapper {

        public static <D extends MachineDefinition> ItemBuilder<MetaMachineItem, MachineBuilder<D>> makeItemBuilder(MachineBuilder<D> builder,
                                                                                                                    BlockEntry<Block> block) {
            return builder.registrate.item(builder, builder.name, properties -> builder.itemFactory.apply((IMachineBlock) block.get(), properties))
                    .setData(ProviderType.LANG, NonNullBiConsumer.noop()) // do not gen any lang keys
                    .model(NonNullBiConsumer::noop)
                    .properties(builder.itemProp);
        }
    }
}
