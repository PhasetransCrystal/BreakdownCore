package com.phasetranscrystal.breacore;

import com.phasetranscrystal.brealib.BreaLib;

import com.phasetranscrystal.breacore.client.ClientProxy;
import com.phasetranscrystal.breacore.common.CommonProxy;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import com.tterrag.registrate.util.RegistrateDistExecutor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BreaCore.MOD_ID)
public class BreaCore {

    public static final Logger LOGGER = LogManager.getLogger("BreaCore:Main");
    public static final String MOD_ID = BreaLib.Core_ID;
    public static final String NAME = "瓦解核心";
    @Getter
    private static ModContainer modContainer;
    @Getter
    private static IEventBus modEventBus;

    public BreaCore(ModContainer container, IEventBus modEventBus) {
        BreaCore.modContainer = container;
        BreaCore.modEventBus = modEventBus;
        RegistrateDistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    public static Logger getLogger(String module) {
        return getLogger(module, null);
    }

    public static Logger getLogger(String module, @Nullable String content) {
        return LogManager.getLogger("BreaCore." + module + (content == null ? "" : ":" + content));
    }
}
