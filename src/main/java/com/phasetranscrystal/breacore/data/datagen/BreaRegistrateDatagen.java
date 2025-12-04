package com.phasetranscrystal.breacore.data.datagen;

import com.phasetranscrystal.breacore.common.registry.BreaRegistration;
import com.phasetranscrystal.breacore.data.datagen.datamap.DataMapsHandler;
import com.phasetranscrystal.breacore.data.datagen.lang.LangHandler;
import com.phasetranscrystal.breacore.data.datagen.tag.BlockTagLoader;
import com.phasetranscrystal.breacore.data.datagen.tag.EntityTypeTagLoader;
import com.phasetranscrystal.breacore.data.datagen.tag.FluidTagLoader;
import com.phasetranscrystal.breacore.data.datagen.tag.ItemTagLoader;

import net.minecraft.data.DataProvider;

import com.tterrag.registrate.providers.ProviderType;

public class BreaRegistrateDatagen {

    public static void init() {
        DataProvider.INDENT_WIDTH.set(4);

        BreaRegistration.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, ItemTagLoader::init);
        BreaRegistration.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BlockTagLoader::init);
        BreaRegistration.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, FluidTagLoader::init);
        BreaRegistration.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, EntityTypeTagLoader::init);
        BreaRegistration.REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::init);
        BreaRegistration.REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, DataMapsHandler::init);
    }
}
