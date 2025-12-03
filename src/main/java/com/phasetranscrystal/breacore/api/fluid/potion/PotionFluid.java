package com.phasetranscrystal.breacore.api.fluid.potion;

import com.phasetranscrystal.breacore.data.fluids.BreaFluids;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PotionFluid extends BaseFlowingFluid {

    public PotionFluid(Properties properties) {
        super(properties
                .bucket(() -> Items.AIR)
                .block(() -> (LiquidBlock) Blocks.WATER));
        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL);
    }

    public static FluidStack of(int amount, Holder<Potion> potion) {
        FluidStack fluidStack = new FluidStack(BreaFluids.POTION.get().getSource(), amount);
        addPotionToFluidStack(fluidStack, potion);
        return fluidStack;
    }

    public static FluidStack withEffects(int amount, Holder<Potion> potion, List<MobEffectInstance> customEffects) {
        FluidStack fluidStack = of(amount, potion);
        appendEffects(fluidStack, customEffects);
        return fluidStack;
    }

    public static FluidStack addPotionToFluidStack(FluidStack fluidStack, Holder<Potion> potion) {
        fluidStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return fluidStack;
    }

    public static FluidStack appendEffects(FluidStack fluidStack, Collection<MobEffectInstance> customEffects) {
        if (customEffects.isEmpty())
            return fluidStack;
        PotionContents contents = fluidStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        List<MobEffectInstance> effects = new ArrayList<>(contents.customEffects());
        effects.addAll(customEffects);
        fluidStack.set(DataComponents.POTION_CONTENTS,
                new PotionContents(contents.potion(), contents.customColor(), effects, contents.customName()));
        return fluidStack;
    }

    public static String getName(Optional<Holder<Potion>> potion, String descriptionId) {
        if (potion.isPresent()) {
            String s = ((Potion) ((Holder) potion.get()).value()).name();
            if (s != null) {
                return descriptionId + s;
            }
        }
        String s1 = (String) potion.flatMap(Holder::unwrapKey)
                .map((key) -> key.location().getPath()).orElse("empty");
        return descriptionId + s1;
    }

    @Override
    public boolean isSource(@NotNull FluidState state) {
        return this == BreaFluids.POTION.get().getSource();
    }

    @Override
    public int getAmount(FluidState state) {
        return state.getValue(LEVEL);
    }

    public static class PotionFluidType extends FluidType {

        /**
         * Default constructor.
         *
         * @param properties the general properties of the fluid type
         */
        public PotionFluidType(Properties properties) {
            super(properties);
        }

        @Override
        public @NotNull String getDescriptionId(FluidStack stack) {
            return getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(),
                    this.getDescriptionId() + ".effect.");
        }
    }
}
