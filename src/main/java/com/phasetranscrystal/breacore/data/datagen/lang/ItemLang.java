package com.phasetranscrystal.breacore.data.datagen.lang;

import com.phasetranscrystal.breacore.api.tag.TagPrefix;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import static com.phasetranscrystal.breacore.data.datagen.lang.LangHandler.replace;

public class ItemLang {

    public static void init(RegistrateLangProvider provider) {
        initGeneratedNames(provider);
        initItemNames(provider);
        initItemTooltips(provider);
    }

    private static void initGeneratedNames(RegistrateLangProvider provider) {
        // TagPrefix
        for (TagPrefix tagPrefix : TagPrefix.values()) {
            provider.add(tagPrefix.getUnlocalizedName(), tagPrefix.langValue);
        }
        provider.add("tagprefix.polymer.plate", "%s Sheet");
        provider.add("tagprefix.polymer.foil", "Thin %s Sheet");
        provider.add("tagprefix.polymer.nugget", "%s Chip");
        provider.add("tagprefix.polymer.dense_plate", "Dense %s Sheet");
        provider.add("tagprefix.polymer.double_plate", "Double %s Sheet");
        provider.add("tagprefix.polymer.tiny_dust", "Tiny Pile of %s Pulp");
        provider.add("tagprefix.polymer.small_dust", "Small Pile of %s Pulp");
        provider.add("tagprefix.polymer.dust", "%s Pulp");
        provider.add("tagprefix.polymer.ingot", "%s Ingot");
    }

    private static void initItemNames(RegistrateLangProvider provider) {
        replace(provider, "item.breacore.tungsten_steel_fluid_cell", "%s Tungstensteel Cell");
    }

    private static void initItemTooltips(RegistrateLangProvider provider) {}
}
