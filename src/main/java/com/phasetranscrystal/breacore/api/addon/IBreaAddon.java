package com.phasetranscrystal.breacore.api.addon;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import com.phasetranscrystal.breacore.api.addon.event.MaterialCasingCollectionEvent;
import com.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface IBreaAddon {

    /**
     * @return this addon's BreaRegistrate instance.
     */
    BreaRegistrate getRegistrate();

    /**
     * This runs after BreaCore has set up it's content. Set up BreaCore loading-dependent (but NOT ones dependent on
     * 
     * @apiNote DO NOT REGISTER ANY OF YOUR OWN CONTENT HERE, AS IF YOU DO, IT'LL REGISTER AS IF BreaCore REGISTERED IT
     *          AND YOUR DATAGEN AND EVENTS WILL <b><i>NOT</i></b> WORK AS EXPECTED, IF AT ALL.
     */
    void breaInitComplete();

    /**
     * Call init on your custom TagPrefix class(es) here
     */
    default void registerTagPrefixes() {}

    /**
     * Call init on your custom IWorldGenLayer class(es) here
     */
    default void registerWorldgenLayers() {}

    /**
     * Call init on your custom VeinGenerator class(es) here
     */
    default void registerVeinGenerators() {}

    /**
     * Call init on your custom IndicatorGenerator class(es) here
     */
    default void registerIndicatorGenerators() {}

    default void addRecipes(RecipeOutput provider) {}

    default void removeRecipes(Consumer<ResourceLocation> consumer) {}

    /**
     * Register Material -> Casing block mappings here
     */
    default void collectMaterialCasings(MaterialCasingCollectionEvent event) {}

    /**
     * Does this addon require high-tier content to be enabled?
     *
     * @return if this addon requires highTier.
     */
    default boolean requiresHighTier() {
        return false;
    }
}
