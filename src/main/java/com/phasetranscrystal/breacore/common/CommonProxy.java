package com.phasetranscrystal.breacore.common;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.BreaAPI;
import com.phasetranscrystal.breacore.api.addon.AddonFinder;
import com.phasetranscrystal.breacore.api.addon.IBreaAddon;
import com.phasetranscrystal.breacore.api.material.event.PostMaterialEvent;
import com.phasetranscrystal.breacore.api.material.registry.MaterialRegistry;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;
import com.phasetranscrystal.breacore.common.horiz.BreaHoriz;
import com.phasetranscrystal.breacore.common.quench.BreaQuench;
import com.phasetranscrystal.breacore.config.ConfigHolder;
import com.phasetranscrystal.breacore.data.blockentity.BreaBlockEntities;
import com.phasetranscrystal.breacore.data.blocks.BreaBlocks;
import com.phasetranscrystal.breacore.data.datagen.BreaRegistrateDatagen;
import com.phasetranscrystal.breacore.data.datagen.lang.MaterialLangGenerator;
import com.phasetranscrystal.breacore.data.entity.BreaEntityTypes;
import com.phasetranscrystal.breacore.data.fluids.BreaFluids;
import com.phasetranscrystal.breacore.data.items.BreaItems;
import com.phasetranscrystal.breacore.data.materials.BreaElements;
import com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet;
import com.phasetranscrystal.breacore.data.materials.BreaMaterialIconTypes;
import com.phasetranscrystal.breacore.data.materials.BreaMaterials;
import com.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;
import com.phasetranscrystal.breacore.data.tagprefix.BreaTagPrefixes;
import com.phasetranscrystal.breacore.mixin.registrate.AbstractRegistrateAccessor;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import com.google.common.collect.Multimaps;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class CommonProxy {

    public CommonProxy() {
        var modBus = BreaCore.getModEventBus();
        ConfigHolder.init();
        BreaAPI.materialManager = BreaRegistries.MATERIALS;
        modBus.register(CommonProxy.class);
        init();

        BreaRegistries.init(modBus);
    }

    public static void init() {
        ConfigHolder.init();
        BreaHoriz.bootstrap();
        BreaQuench.bootstrap();

        BreaElements.init();
        BreaMaterialIconSet.init();
        BreaMaterialIconTypes.init();
        initMaterials();
        BreaTagPrefixes.init();

        BreaFluids.init();
        BreaCreativeModeTabs.init();
        BreaBlocks.init();
        BreaEntityTypes.init();
        BreaBlockEntities.init();

        BreaItems.init();

        AddonFinder.getAddonList().forEach(IBreaAddon::breaInitComplete);

        BreaRegistrateDatagen.init();
        // Register all material manager registries, for materials with mod ids.
        BreaAPI.materialManager.getUsedNamespaces().forEach(namespace -> {
            // Force the material lang generator to be at index 0, so that addons' lang generators can override it.
            BreaRegistrate registrate = BreaRegistrate.createIgnoringListenerErrors(namespace);
            AbstractRegistrateAccessor accessor = (AbstractRegistrateAccessor) registrate;
            if (accessor.getDoDatagen().get()) {
                List<NonNullConsumer<? extends RegistrateProvider>> providers = Multimaps.asMap(accessor.getDatagens()).get(ProviderType.LANG);
                providers.addFirst((provider) -> MaterialLangGenerator.generate((RegistrateLangProvider) provider, namespace));
            }

            ModList.get().getModContainerById(namespace).map(ModContainer::getEventBus).ifPresent(registrate::registerEventListeners);
        });
    }

    public static void registry() {
        BreaCreativeModeTabs.init();
        BreaFluids.init();
        BreaBlocks.init();
        BreaItems.init();
    }

    private static void initMaterials() {
        MaterialRegistry managerInternal = (MaterialRegistry) BreaAPI.materialManager;
        managerInternal.unfreezeRegistries();
        BreaCore.LOGGER.info("Registering Materials");
        BreaMaterials.init();
        managerInternal.setFallbackMaterial(BreaCore.MOD_ID, BreaMaterials.Aluminium);
        BreaCore.LOGGER.info("Registering addon Materials");
        BreaAPI.postRegisterEvent(BreaRegistries.MATERIALS);
        // Fire Post-Material event, intended for when Materials need to be iterated over in-full before freezing
        // Block entirely new Materials from being added in the Post event
        managerInternal.closeRegistries();
        ModLoader.postEventWrapContainerInModOrder(new PostMaterialEvent());

        // Freeze Material Registry before processing Items, Blocks, and Fluids
        managerInternal.freezeRegistries();
        /* End Material Registration */
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {}

    @SubscribeEvent
    public static void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {}

    @SubscribeEvent
    public static void registerPackFinders(AddPackFindersEvent event) {}

    @SubscribeEvent
    public static void addValidBlocksToBETypes(BlockEntityTypeAddBlocksEvent event) {}
}
