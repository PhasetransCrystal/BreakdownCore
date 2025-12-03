package com.phasetranscrystal.breacore.api.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import com.phasetranscrystal.breacore.api.fluid.BreaFluid;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.property.PropertyKey;
import org.jetbrains.annotations.Nullable;

public class MaterialBucketItem extends BucketItem {

    final Material material;
    final String langKey;

    public MaterialBucketItem(Fluid fluid, Properties properties, Material material, String langKey) {
        super(fluid, properties);
        this.material = material;
        this.langKey = langKey;
    }

    public static int color(ItemStack itemStack, int index) {
        if (itemStack.getItem() instanceof MaterialBucketItem item) {
            if (index == 1) {
                return IClientFluidTypeExtensions.of(item.content).getTintColor();
            }
        }
        return -1;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component materialName = material.getLocalizedName();
        return Component.translatable("item.breacore.bucket", Component.translatable(this.langKey, materialName));
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        var property = material.getProperty(PropertyKey.FLUID);
        if (property != null) {
            var fluid = material.getFluid();
            if (fluid instanceof BreaFluid bFluid) {
                return bFluid.getBurnTime();
            }
        }
        return 0;
    }
}
