package com.phasetranscrystal.breacore.common.blast;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.BreaAPI;
import com.phasetranscrystal.breacore.api.blast.player.KeyInputPacket;
import com.phasetranscrystal.breacore.api.blast.player.SkillDataSynPacket;
import com.phasetranscrystal.breacore.api.blast.player.SkillGroup;
import com.phasetranscrystal.breacore.api.blast.skill.Skill;
import com.phasetranscrystal.breacore.api.blast.skill.SkillData;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

@EventBusSubscriber
public class BreaBlast {

    static {
        BreaRegistries.SKILLS.unfreeze(true);
    }

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<SkillGroup>> PLAYER_SKILL_GROUP;

    public static final RegistryEntry<Attribute, RangedAttribute> SKILL_INACTIVE_ENERGY;
    public static final RegistryEntry<Attribute, RangedAttribute> SKILL_ACTIVE_ENERGY;
    public static final RegistryEntry<Attribute, RangedAttribute> SKILL_MAX_CHARGE;

    public static Skill<? extends Entity> EMPTY_SKILL;
    public static Skill<Player> TEST_SKILL;
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

        EMPTY_SKILL = BreaRegistries.SKILLS.register(BreaUtil.byPath("blast/empty"), Skill.EMPTY);
        TEST_SKILL = BreaRegistries.SKILLS.register(BreaUtil.byPath("blast/test"),
                Skill.Builder.<Player>of(30, "inactive")
                        .start(data -> data.getEntity().displayClientMessage(Component.literal("TestSkillInit"), false))
                        // .flag(Skill.Flag.INSTANT_COMPLETE, true)
                        .onEvent(EntityTickEvent.Post.class, (event, data) -> {
                            Player player = data.getEntity();
                            if (player.level().getGameTime() % 100 == 0 && player.getHealth() < player.getMaxHealth()) {
                                player.displayClientMessage(Component.literal("You are healed!"), false);
                                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2));
                            }
                        })
                        .inactive(builder -> builder
                                // 按键监听测试
                                .onHurt((event, data) -> data.addEnergy(-1))
                                .onAttack((event, data) -> data.addEnergy(2))
                                .onKillTarget((event, data) -> data.addEnergy(5))
                                .energyChanged((data, i) -> {
                                    data.getEntity().displayClientMessage(Component.literal("Energy " + (i >= 0 ? "§a+" : "§c-") + i), true);
                                })
                                .chargeChanged((data, i) -> {
                                    data.getEntity().displayClientMessage(Component.literal("Charge " + (i >= 0 ? "§a+" : "§c-") + i), true);
                                    ResourceLocation location = ResourceLocation.fromNamespaceAndPath(BreaLib.Core_ID, "blast/skill_test");
                                    data.addAutoCleanAttribute(new AttributeModifier(location, 0.5 * data.getCharge(), AttributeModifier.Operation.ADD_VALUE), Attributes.MOVEMENT_SPEED);
                                })
                                .onChargeReady(data -> data.getEntity().displayClientMessage(Component.literal("ReachReady!"), false))
                                .onChargeFull(data -> data.getEntity().displayClientMessage(Component.literal("ReachStop!"), false))
                                .endWith(data -> {
                                    data.putCacheData("charge_consume", data.getCharge() + 1 + "", true, true);
                                }))
                        .judge((data, name) -> !"active".equals(name) || (data.getEntity().level().isDarkOutside() && data.getCharge() >= 1))
                        .active(builder -> builder
                                .startWith(data -> {
                                    Vec3 pos = data.getEntity().position();
                                    ((ServerLevel) data.getEntity().level()).sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 4, 0.5, 0.5, 0.5, 0.5);
                                    data.setForegroundEnergy(40);
                                })
                                .onTick((event, data) -> {
                                    data.getEntity().displayClientMessage(Component.literal("activeTick:" + data.getForegroundEnergy()), true);
                                    data.addEnergy(-1);
                                })
                                .endWith(data -> {
                                    data.getEntity().jumpFromGround();
                                    data.getEntity().addDeltaMovement(new Vec3(0, 0.1, 0));
                                    data.getEntity().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200 * data.getCacheDataAsInt("charge_consume", 0, true), 2));
                                }))
                        .onBehaviorChange((data, behavior) -> {
                            if (data.getActiveTimes() == 5) data.requestDisable();
                            else {
                                data.getEntity().displayClientMessage(Component.literal("StateChanged: to \"" + behavior + "\", times: " + data.getActiveTimes()), false);
                                if ("active".equals(behavior)) data.consumeCharge();
                            }
                        })
                        .onEnd(data -> data.getEntity().displayClientMessage(Component.literal("skill disabled"), false))
                        .build(Player.class)

        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        event.getEntity().getExistingData(BreaBlast.PLAYER_SKILL_GROUP).map(SkillGroup::getCurrentSkillData).ifPresent(SkillData::requestDisable);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
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

    public static void bootstrap() {
        BreaRegistries.SKILLS.freeze();
        BreaAPI.postRegisterEvent(BreaRegistries.SKILLS);
    }
}
