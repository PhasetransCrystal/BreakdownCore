package com.phasetranscrystal.breacore.common.blast;

import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.common.blast.player.KeyInputPacket;
import com.phasetranscrystal.breacore.common.blast.player.SkillDataSynPacket;
import com.phasetranscrystal.breacore.common.blast.player.SkillGroup;
import com.phasetranscrystal.breacore.common.blast.skill.Skill;
import com.phasetranscrystal.breacore.common.blast.skill.SkillData;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

@EventBusSubscriber
public class BreaBlast {

    public static void bootstrap() {
        BreaBlastTest.bootstrap();
    }

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<SkillGroup>> PLAYER_SKILL_GROUP;

    public static final RegistryEntry<Attribute, RangedAttribute> SKILL_INACTIVE_ENERGY;
    public static final RegistryEntry<Attribute, RangedAttribute> SKILL_ACTIVE_ENERGY;
    public static final RegistryEntry<Attribute, RangedAttribute> SKILL_MAX_CHARGE;

    public static final RegistryEntry<Skill<?>, Skill<? extends Entity>> EMPTY_SKILL;

    static {
        PLAYER_SKILL_GROUP = REGISTRATE.simple("blast/player_skill_group",
                NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                () -> AttachmentType.builder(SkillGroup::new).serialize(SkillGroup.CODEC).copyOnDeath().build());
        SKILL_INACTIVE_ENERGY = REGISTRATE.simple("blast/inactive_energy",
                Registries.ATTRIBUTE,
                () -> new RangedAttribute("attribute.name.skill.inactive_energy", 0, 0, Integer.MAX_VALUE));
        SKILL_ACTIVE_ENERGY = REGISTRATE.simple("blast/active_energy",
                Registries.ATTRIBUTE,
                () -> new RangedAttribute("attribute.name.skill.active_energy", 0, 0, Integer.MAX_VALUE));
        SKILL_MAX_CHARGE = REGISTRATE.simple("blast/max_charge",
                Registries.ATTRIBUTE,
                () -> new RangedAttribute("attribute.name.skill.max_charge", 1, 1, Integer.MAX_VALUE));
        EMPTY_SKILL = REGISTRATE.simple("blast/empty",
                BreaRegistries.SKILL_KEY,
                () -> Skill.EMPTY);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        event.getEntity().getExistingData(BreaBlast.PLAYER_SKILL_GROUP).map(SkillGroup::getCurrentSkillData).ifPresent(SkillData::requestDisable);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void init(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            SkillGroup data = serverPlayer.getData(BreaBlast.PLAYER_SKILL_GROUP);
            data.bindEntity(serverPlayer);
            data.getCurrentSkillData().requestEnable();
        }
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                KeyInputPacket.TYPE,
                KeyInputPacket.STREAM_CODEC,
                KeyInputPacket::serverHandler);

        registrar.playToClient(
                SkillDataSynPacket.TYPE,
                SkillDataSynPacket.STREAM_CODEC,
                SkillDataSynPacket::consume);
    }
}
