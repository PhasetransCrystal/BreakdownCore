package com.phasetranscrystal.breacore.data.items;

import com.phasetranscrystal.breacore.api.item.MuiItem;
import com.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;

import static com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate.Brea;
import static com.phasetranscrystal.breacore.data.items.BreaItems.*;

public class DebugItems {

    static {
        Brea.defaultCreativeTab(BreaCreativeModeTabs.DEBUG_ITEMS.getKey());
    }

    public static void init() {
        MUI_ITEM = Brea.item("mui_item", MuiItem::new).register();
    }
}
