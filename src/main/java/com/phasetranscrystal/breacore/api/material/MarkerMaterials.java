package com.phasetranscrystal.breacore.api.material;

import net.minecraft.world.item.DyeColor;

import com.google.common.collect.HashBiMap;
import com.phasetranscrystal.brealib.utils.BreaUtil;

public class MarkerMaterials {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        Color.Colorless.toString();
        Empty.toString();
    }

    /**
     * 无分类的标记材料
     */
    public static final MarkerMaterial Empty = new MarkerMaterial(BreaUtil.byPath("empty"));

    /**
     * 颜色材料
     */
    public static class Color {

        /**
         * 只能通过直接指定使用
         * 表示 TagPrefix 上没有颜色
         * 通常作为颜色前缀的默认值
         */
        public static final MarkerMaterial Colorless = new MarkerMaterial(BreaUtil.byPath("colorless"));

        public static final MarkerMaterial White = new MarkerMaterial(BreaUtil.byPath("white"));
        public static final MarkerMaterial Orange = new MarkerMaterial(BreaUtil.byPath("orange"));
        public static final MarkerMaterial Magenta = new MarkerMaterial(BreaUtil.byPath("magenta"));
        public static final MarkerMaterial LightBlue = new MarkerMaterial(BreaUtil.byPath("light_blue"));
        public static final MarkerMaterial Yellow = new MarkerMaterial(BreaUtil.byPath("yellow"));
        public static final MarkerMaterial Lime = new MarkerMaterial(BreaUtil.byPath("lime"));
        public static final MarkerMaterial Pink = new MarkerMaterial(BreaUtil.byPath("pink"));
        public static final MarkerMaterial Gray = new MarkerMaterial(BreaUtil.byPath("gray"));
        public static final MarkerMaterial LightGray = new MarkerMaterial(BreaUtil.byPath("light_gray"));
        public static final MarkerMaterial Cyan = new MarkerMaterial(BreaUtil.byPath("cyan"));
        public static final MarkerMaterial Purple = new MarkerMaterial(BreaUtil.byPath("purple"));
        public static final MarkerMaterial Blue = new MarkerMaterial(BreaUtil.byPath("blue"));
        public static final MarkerMaterial Brown = new MarkerMaterial(BreaUtil.byPath("brown"));
        public static final MarkerMaterial Green = new MarkerMaterial(BreaUtil.byPath("green"));
        public static final MarkerMaterial Red = new MarkerMaterial(BreaUtil.byPath("red"));
        public static final MarkerMaterial Black = new MarkerMaterial(BreaUtil.byPath("black"));
        /**
         * 包含所有可能颜色值的数组（不包含无色！）
         */
        public static final MarkerMaterial[] VALUES = new MarkerMaterial[] {
                White, Orange, Magenta, LightBlue, Yellow, Lime, Pink, Gray, LightGray, Cyan, Purple, Blue, Brown,
                Green, Red, Black
        };

        /**
         * 通过颜色名称获取颜色材料
         * 名称格式与 DyeColor 相同
         */
        public static MarkerMaterial valueOf(String string) {
            for (MarkerMaterial color : VALUES) {
                if (color.getName().equals(string)) {
                    return color;
                }
            }
            return null;
        }

        /**
         * 包含 MC DyeColor 与颜色标记材料之间的关联映射
         */
        public static final HashBiMap<DyeColor, MarkerMaterial> COLORS = HashBiMap.create();

        static {
            for (var color : DyeColor.values()) {
                COLORS.put(color, Color.valueOf(color.getName()));
            }
        }
    }
}
