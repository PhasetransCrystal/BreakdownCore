package com.phasetranscrystal.breacore.common.blast.player;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.common.blast.BreaBlast;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import io.netty.buffer.ByteBuf;

public record KeyInputPacket(short var) implements CustomPacketPayload {

    public static final Type<KeyInputPacket> TYPE = new Type<>(BreaUtil.byPath("blast/key_input"));
    public static final StreamCodec<ByteBuf, KeyInputPacket> STREAM_CODEC = ByteBufCodecs.SHORT.map(KeyInputPacket::new, KeyInputPacket::var);

    public KeyInputPacket(int key, int modifier, int action) {
        this((short) (((action & 0b1) << 15) | ((modifier & 0x3F) << 9) | (key & 0x1FF)));
    }

    public int key() {
        return var & 0x1FF;
    }

    public int modifier() {
        return (var >>> 9) & 0x3F;
    }

    public int action() {
        return (var >>> 15) & 0b1;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void serverHandler(KeyInputPacket pack, IPayloadContext context) {
        context.player().getData(BreaBlast.PLAYER_SKILL_GROUP).consumeInputPacket(pack);
    }
}
