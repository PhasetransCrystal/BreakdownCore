package com.phasetranscrystal.breacore.utils;

import net.minecraft.client.resources.language.I18n;

import com.phasetranscrystal.brealib.utils.BreaUtil;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class LocalizationUtils {

    private final static Map<String, String> DYNAMIC_LANG = new HashMap<>();

    public static void appendDynamicLang(Map<String, String> dynamicLang) {
        DYNAMIC_LANG.putAll(dynamicLang);
    }

    public static boolean hasDynamicLang(String key) {
        return DYNAMIC_LANG.containsKey(key);
    }

    public static String getDynamicLang(String key) {
        return DYNAMIC_LANG.get(key);
    }

    /**
     * 此函数在客户端调用时调用 `net.minecraft.client.resources.I18n.format`，
     * 在服务端调用时调用 `net.minecraft.util.text.translation.I18n.translateToLocalFormatted`。
     * <ul>
     * <li>客户端的翻译应使用 `I18n` 完成。</li>
     * <li>服务端设置翻译应使用 `TextComponentTranslatable`。</li>
     * <li>`LocalisationUtils` 仅用于服务端需要某种翻译且没有客户端/玩家上下文的情况。</li>
     * <li>`LocalisationUtils` 是"best effort"的，可能只能正确处理 en-us。</li>
     * </ul>
     *
     * @param localisationKey 传递给底层格式化函数的本地化键
     * @param substitutions   传递给底层格式化函数的替换参数
     * @return 本地化后的字符串
     */
    public static String format(String localisationKey, Object... substitutions) {
        throw new NotImplementedException();
    }

    /**
     * 此函数在客户端调用时调用 `net.minecraft.client.resources.I18n.hasKey`，
     * 在服务端调用时调用 `net.minecraft.util.text.translation.I18n.canTranslate`。
     * <ul>
     * <li>客户端的翻译应使用 `I18n` 完成。</li>
     * <li>服务端设置翻译应使用 `TextComponentTranslatable`。</li>
     * <li>`LocalisationUtils` 仅用于服务端需要某种翻译且没有客户端/玩家上下文的情况。</li>
     * <li>`LocalisationUtils` 是"尽力而为"的，可能只能正确处理 en-us。</li>
     * </ul>
     *
     * @param localisationKey 传递给底层 hasKey 函数的本地化键
     * @return 布尔值，表示给定本地化键是否有本地化内容
     */
    public static boolean exist(String localisationKey) {
        if (BreaUtil.isClientSide()) {
            return I18n.exists(localisationKey);
        } else {
            return false;
        }
    }
}
