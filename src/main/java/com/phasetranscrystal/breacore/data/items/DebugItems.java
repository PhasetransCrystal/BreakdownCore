package com.phasetranscrystal.breacore.data.items;

import com.phasetranscrystal.breacore.api.item.debug.MuiItem;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static com.phasetranscrystal.breacore.data.items.BreaItems.*;
import static com.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs.DEBUG_ITEMS;

public class DebugItems {

    static {
        REGISTRATE.creativeModeTab(() -> DEBUG_ITEMS);
    }

    public static void init() {
        MUI_ITEM = REGISTRATE.item("mui_item", MuiItem::new)
                .lang("Mui Test")
                .register();
    }
}
