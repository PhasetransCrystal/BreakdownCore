package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.breacore.common.horiz.EntityDistributorInit;
import com.phasetranscrystal.breacore.common.horiz.EventDistributor;
import com.phasetranscrystal.breacore.common.horiz.EventDistributorTest;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate.Brea;

public class BreaQuench {

    public static final String MODULE_ID = "horiz";
    public static final String MODULE_NAME = "Horiz";

    public static void bootstrap(IEventBus bus) {
        EntityDistributorInit.bootstrapConsumer();
        if (BreaUtil.isProd()) {
            EventDistributorTest.bootstrapConsumer(bus);
        }
    }

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<EventDistributor>> EVENT_DISTRIBUTOR;
    static {
        EVENT_DISTRIBUTOR = Brea.simple("horiz/event_distributor",
                NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                () -> AttachmentType.builder(holder -> new EventDistributor()).serialize(EventDistributor.CODEC).build());
    }
}
