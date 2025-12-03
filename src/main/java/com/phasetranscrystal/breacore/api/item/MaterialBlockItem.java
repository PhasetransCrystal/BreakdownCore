package com.phasetranscrystal.breacore.api.item;

import com.phasetranscrystal.breacore.api.BreaAPI;
import com.phasetranscrystal.breacore.api.block.MaterialBlock;
import com.phasetranscrystal.breacore.api.material.property.DustProperty;
import com.phasetranscrystal.breacore.api.material.property.PropertyKey;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaterialBlockItem extends BlockItem {

    public MaterialBlockItem(MaterialBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        return getItemBurnTime();
    }

    @Override
    @NotNull
    public MaterialBlock getBlock() {
        return (MaterialBlock) super.getBlock();
    }

    @Override
    public Component getName(ItemStack stack) {
        return getBlock().getName();
    }

    public int getItemBurnTime() {
        var material = getBlock().material;
        DustProperty property = material.isNull() ? null : material.getProperty(PropertyKey.DUST);
        if (property != null)
            return (int) (property.getBurnTime() * getBlock().tagPrefix.getMaterialAmount(material) / BreaAPI.M);
        return 0;
    }
}
