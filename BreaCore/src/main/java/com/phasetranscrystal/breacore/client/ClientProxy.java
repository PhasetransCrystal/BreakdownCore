package com.phasetranscrystal.breacore.client;

import com.phasetranscrystal.breacore.client.datagen.TextureCreater;
import com.phasetranscrystal.breacore.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        TextureCreater.init();
    }
}
