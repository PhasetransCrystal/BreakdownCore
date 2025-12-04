package com.phasetranscrystal.breacore.mixin.neoforge;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.material.ItemMaterialData;

import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = ReloadableServerResources.class, priority = 2000)
public abstract class ReloadableServerResourcesMixin {

    @Inject(method = "loadResources", at = @At("HEAD"), remap = false)
    private static void breacore$init(ResourceManager resourceManager,
                                      LayeredRegistryAccess<RegistryLayer> registryAccess,
                                      List<Registry.PendingTags<?>> postponedTags,
                                      FeatureFlagSet enabledFeatures,
                                      Commands.CommandSelection commandSelection,
                                      int functionCompilationLevel,
                                      Executor backgroundExecutor,
                                      Executor gameExecutor,
                                      CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {
        // load and loot tables recipes *before* other data so that we have the registries loaded
        // before saving recipes to JSON.
        // because it breaks if we don't do that.

        // this doesn't have dynamic registries available, by the way.
        RegistryAccess.Frozen frozen = registryAccess.compositeAccess();

        // Register recipes & unification data again
        long startTime = System.currentTimeMillis();
        ItemMaterialData.reinitializeMaterialData();
        // GTCraftingComponents.init();
        /*
         * GTRecipes.recipeAddition(new RecipeOutput() {
         * 
         * @Override
         * public Advancement.@NotNull Builder advancement() {
         * // noinspection removal
         * return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
         * }
         * 
         * @Override
         * public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe,
         * 
         * @Nullable AdvancementHolder advancement, ICondition @NotNull ... conditions) {
         * GTDynamicDataPack.addRecipe(id, recipe, advancement, frozen);
         * }
         * });
         */
        // MixinHelpers.generateGTDynamicLoot(GTDynamicDataPack::addLootTable, frozen);
        // Initialize dungeon loot additions
        // DungeonLootLoader.init();
        BreaCore.LOGGER.info("Data loading took {}ms", System.currentTimeMillis() - startTime);
    }
}
