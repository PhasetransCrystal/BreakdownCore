package com.phasetranscrystal.breacore.common.blast;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.blast.player.SkillGroup;
import com.phasetranscrystal.breacore.api.blast.skill.Skill;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class BreaBlastTest {

    public static void bootstrap() {
        NeoForge.EVENT_BUS.addListener(BreaBlastTest::testSkillInit);

        BreaBlast.TEST_SKILL = BreaRegistries.SKILLS.register(BreaUtil.byPath("blast/test"),
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
                        .build(Player.class));
    }

    public static void testSkillInit(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            SkillGroup data = serverPlayer.getData(BreaBlast.PLAYER_SKILL_GROUP);
            data.unlock(BreaBlast.TEST_SKILL);
            data.changeTo(BreaBlast.TEST_SKILL);
        }
    }

    // public static final DeferredHolder<Skill<?>, Skill<Player>> OLD_MA = SKILL.register("old_ma", () -> Skill.Builder
    // .<Player>of(50, 3, 0, 0, 50)
    // .start(data -> data.getEntity().displayClientMessage(Component.literal("OldMaInit"), false))
    // .judge((data, name) -> data.getCharge() == 3)
    // .addBehavior(builder -> builder
    // .onKeyInput((data, pack) -> data.getEntity().sendSystemMessage(Component.literal("按键拦截成功")), GLFW.GLFW_KEY_H)
    // .endWith(data -> data.getEntity().displayClientMessage(Component.literal("OldMaEnd"), false)),
    // "key test")
    // .build(Player.class));

    public static class Start extends Item {

        public Start() {
            super(new Properties());
        }

        @Override
        public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
            if (level instanceof ServerLevel server) {
                if (usedHand == InteractionHand.OFF_HAND && !player.getData(BreaBlast.PLAYER_SKILL_GROUP).getCurrentSkillData().isEnabled()) {
                    player.getData(BreaBlast.PLAYER_SKILL_GROUP).getCurrentSkillData().requestEnable();
                } else {
                    player.getData(BreaBlast.PLAYER_SKILL_GROUP).getCurrentSkillData().switchToIfNot("active");
                }
            }
            return InteractionResult.SUCCESS;
        }
    }
}
