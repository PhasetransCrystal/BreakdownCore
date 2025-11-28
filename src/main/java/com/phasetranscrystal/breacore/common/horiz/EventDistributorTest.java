package com.phasetranscrystal.breacore.common.horiz;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.phasetranscrystal.breacore.BreaCore;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import lombok.Getter;

@EventBusSubscriber(modid = BreaCore.MOD_ID)
public class EventDistributorTest {

    public static void bootstrapConsumer(IEventBus bus) {
        bus.addListener(EventDistributorTest::bingingToPlayer);
    }

    public static class LoginListener extends SavableEventConsumerData<PlayerEvent.PlayerLoggedInEvent> {

        public static final MapCodec<LoginListener> CODEC = Codec.STRING.xmap(LoginListener::new, LoginListener::getContent).fieldOf("text");
        @Getter
        public final String content;

        public LoginListener(String content) {
            this.content = content;
        }

        @Override
        public Class<PlayerEvent.PlayerLoggedInEvent> getEventClass() {
            return PlayerEvent.PlayerLoggedInEvent.class;
        }

        @Override
        protected void consumeEvent(PlayerEvent.PlayerLoggedInEvent event) {
            // 处理玩家登录事件
            event.getEntity().displayClientMessage(Component.literal(content), false);
        }

        // 配置是否允许处理取消的事件
        @Override
        public boolean handleCancelled() {
            return false;
        }

        @Override
        public MapCodec<LoginListener> getCodec() {
            return CODEC;
        }
    }

    public static final MapCodec<LoginListener> LOGIN_SHOW_TEXT;

    private static void bingingToPlayer(EntityDistributorInit.GatherEntityDistributeEvent event) {
        if (event.getEntity() instanceof Player) {
            event.getEntity().getData(BreaHoriz.EVENT_DISTRIBUTOR).add(new LoginListener("HELLO PLAYER!"), BreaUtil.byPath("testing"));
        }
    }

    static {
        BreaRegistries.SAVABLE_EVENT_CONSUMER_TYPE.unfreeze(false);
        LOGIN_SHOW_TEXT = BreaRegistries.SAVABLE_EVENT_CONSUMER_TYPE.register(BreaUtil.byPath("horiz/login_show_text"), LoginListener.CODEC);
        BreaRegistries.SAVABLE_EVENT_CONSUMER_TYPE.freeze();
    }
}
