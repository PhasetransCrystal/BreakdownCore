package com.phasetranscrystal.breacore.data.items;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.LayeredCauldronBlock;

import com.phasetranscrystal.breacore.api.item.ComponentItem;
import com.phasetranscrystal.breacore.api.item.IComponentItem;
import com.phasetranscrystal.breacore.api.item.TagPrefixItem;
import com.phasetranscrystal.breacore.api.item.component.IItemComponent;
import com.phasetranscrystal.breacore.api.item.debug.MuiItem;
import com.phasetranscrystal.breacore.api.material.ChemicalHelper;
import com.phasetranscrystal.breacore.api.material.ItemMaterialData;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.stack.ItemMaterialInfo;
import com.phasetranscrystal.breacore.api.material.stack.MaterialEntry;
import com.phasetranscrystal.breacore.api.tag.TagPrefix;
import com.phasetranscrystal.breacore.utils.memoization.CacheMemoizer;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BreaItems {

    public static ItemEntry<MuiItem> MUI_ITEM;

    public static void init() {
        DebugItems.init();
        MaterialItems.init();
    }

    public static <T extends ItemLike> NonNullConsumer<T> materialInfo(ItemMaterialInfo materialInfo) {
        return item -> ItemMaterialData.registerMaterialInfo(item, materialInfo);
    }

    public static <P, T extends Item, S2 extends ItemBuilder<T, P>> NonNullFunction<S2, S2> unificationItem(@NotNull TagPrefix tagPrefix, @NotNull Material mat) {
        return builder -> {
            builder.onRegister(item -> {
                Supplier<ItemLike> supplier = CacheMemoizer.memoize(() -> item);
                MaterialEntry entry = new MaterialEntry(tagPrefix, mat);
                MaterialItems.toUnify.put(entry, supplier);
                ItemMaterialData.registerMaterialEntry(supplier, entry);
            });
            return builder;
        };
    }

    public static <T extends Item> void cauldronInteraction(T item) {
        if (item instanceof TagPrefixItem tagPrefixItem &&
                MaterialItems.purifyMap.containsKey(tagPrefixItem.tagPrefix)) {
            CauldronInteraction.WATER.map().put(item, (state, world, pos, player, hand, stack) -> {
                if (!world.isClientSide) {
                    Item stackItem = stack.getItem();
                    if (stackItem instanceof TagPrefixItem prefixItem) {
                        if (!MaterialItems.purifyMap.containsKey(prefixItem.tagPrefix))
                            return InteractionResult.PASS;
                        if (!state.hasProperty(LayeredCauldronBlock.LEVEL)) {
                            return InteractionResult.PASS;
                        }

                        int level = state.getValue(LayeredCauldronBlock.LEVEL);
                        if (level == 0)
                            return InteractionResult.PASS;

                        player.setItemInHand(hand, ChemicalHelper.get(MaterialItems.purifyMap.get(prefixItem.tagPrefix), prefixItem.material, stack.getCount()));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.awardStat(Stats.ITEM_USED.get(stackItem));
                        LayeredCauldronBlock.lowerFillLevel(state, world, pos);

                    }
                }

                return world.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
            });

        }
    }

    public static <T extends ComponentItem> NonNullConsumer<T> burnTime(int burnTime) {
        return item -> item.burnTime(burnTime);
    }

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent components) {
        return item -> item.attachComponents(components);
    }

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent... components) {
        return item -> item.attachComponents(components);
    }

    @NotNull
    private static <
            T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateLangProvider> reverseLangValue() {
        return (ctx, prov) -> {
            var names = Arrays.stream(ctx.getName().split("/.")).collect(Collectors.toList());
            Collections.reverse(names);
            prov.add(ctx.get(), names.stream().map(StringUtils::capitalize).collect(Collectors.joining(" ")));
        };
    }
}
