package com.phasetranscrystal.breacore.common;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate;
import com.phasetranscrystal.breacore.common.horiz.BreaHoriz;
import com.phasetranscrystal.breacore.config.ConfigHolder;
import com.phasetranscrystal.breacore.data.blocks.BreaBlocks;
import com.phasetranscrystal.breacore.data.fluids.BreaFluids;
import com.phasetranscrystal.breacore.data.items.BreaItems;
import com.phasetranscrystal.breacore.data.materials.BreaMaterials;
import com.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonProxy {

    public CommonProxy() {
        init();
        IEventBus eventBus = BreaCore.getModEventBus();
        ConfigHolder.init();
        BreaRegistrate.Brea.registerEventListeners(eventBus);
        BreaRegistries.init(eventBus);
        BreaHoriz.bootstrap(eventBus);
        eventBus.addListener(CommonProxy::onCommonSetup);
        registry();
    }

    public static void init() {}

    public static void registry() {
        BreaCreativeModeTabs.init();
        BreaMaterials.init();
        BreaFluids.init();
        BreaBlocks.init();
        BreaItems.init();
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        log.info("Common Setup");
    }
}
