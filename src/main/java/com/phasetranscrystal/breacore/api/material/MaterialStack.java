package com.phasetranscrystal.breacore.api.material;

import com.phasetranscrystal.breacore.data.materials.BreaMaterials;
import com.phasetranscrystal.brealib.utils.FormattingUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public record MaterialStack(@NotNull Material material, long amount) {

    public static final MaterialStack EMPTY = new MaterialStack(BreaMaterials.NULL, 0);

    private static final Map<String, MaterialStack> PARSE_CACHE = new WeakHashMap<>();

    public MaterialStack copy() {
        if (isEmpty()) return EMPTY;
        return new MaterialStack(material, amount);
    }

    public static MaterialStack fromString(CharSequence str) {
        String trimmed = str.toString().trim();
        String copy = trimmed;

        var cached = PARSE_CACHE.get(trimmed);

        if (cached != null) {
            return cached;
        }

        var count = 1;
        var spaceIndex = copy.indexOf(' ');

        if (spaceIndex >= 2 && copy.indexOf('x') == spaceIndex - 1) {
            count = Integer.parseInt(copy.substring(0, spaceIndex - 1));
            copy = copy.substring(spaceIndex + 1);
        }

        cached = new MaterialStack(BreaMaterials.get(copy), count);
        PARSE_CACHE.put(trimmed, cached);
        return cached;
    }

    public boolean isEmpty() {
        return this.material == BreaMaterials.NULL || this.amount < 1;
    }

    @Override
    public @NotNull String toString() {
        String string = "";
        if (this.isEmpty()) return "";
        if (material.getChemicalFormula() == null || material.getChemicalFormula().isEmpty()) {
            string += "?";
        } else if (material.getMaterialComponents().size() > 1) {
            string += '(' + material.getChemicalFormula() + ')';
        } else {
            string += material.getChemicalFormula();
        }
        if (amount > 1) {
            string += FormattingUtil.toSmallDownNumbers(Long.toString(amount));
        }
        return string;
    }
}
