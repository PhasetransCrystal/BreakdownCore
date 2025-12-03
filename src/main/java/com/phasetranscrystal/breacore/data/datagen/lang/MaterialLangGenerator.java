package com.phasetranscrystal.breacore.data.datagen.lang;

import com.phasetranscrystal.breacore.api.BreaAPI;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import static com.phasetranscrystal.brealib.utils.FormattingUtil.toEnglishName;

public class MaterialLangGenerator {

    public static void generate(RegistrateLangProvider provider, final String modId) {
        BreaAPI.materialManager.stream()
                .filter(mat -> mat.getModid().equals(modId))
                .forEach(material -> {
                    provider.add(material.getUnlocalizedName(), toEnglishName(material.getName()));
                });
    }
}
