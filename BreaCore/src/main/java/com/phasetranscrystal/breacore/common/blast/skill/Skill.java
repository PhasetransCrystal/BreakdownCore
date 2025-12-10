package com.phasetranscrystal.breacore.common.blast.skill;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.common.blast.player.KeyInput;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;

import net.neoforged.bus.api.Event;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

// 随记：非激活表现的end区需自行消耗充能，激活表现的start区需要自行初始化能量值
public class Skill<T extends Entity> {

    public static final Skill<Entity> EMPTY = Builder.of(0, "default").addBehavior("default", b -> {}).build(Entity.class);

    public static final Logger LOGGER = BreaLib.getLogger("Blast", "Skill");
    public static final ResourceLocation NAME = BreaUtil.byPath("blast/skill");

    public final int initialEnergy;// 初始能量

    @Nonnull
    public final Behavior<T> initBehavior;
    public final String initBehaviorName;// 初始阶段
    public final ImmutableMap<String, Behavior<T>> behaviors;// 技能内阶段

    public final Consumer<SkillData<T>> onEnable;// 技能启用
    public final Consumer<SkillData<T>> onDiable;// 技能禁用
    // 第二个参数(String)为将被转换到的状态的id
    public final ToBooleanBiFunction<SkillData<T>, String> judge;// 阶段切换判定器 数据-将进入的阶段名->是否允许
    public final BiConsumer<SkillData<T>, String> stateChange;// 阶段将要切换时触发 旧数据已被清除，接下来就是处理交换能量与载入新数据
    public final KeyInput.Consumer<T> keyChange;// 处理按键输入(待测试)

    public final IntList keys;// 按键拦截列表
    public final ImmutableMap<Class<? extends Event>, BiConsumer<? extends Event, SkillData<T>>> listeners;// 全局事件监听器
    // public final ImmutableSet<Flag> flags;

    public final Class<T> bindingEntityClass;

    public Skill(Builder<T> builder, Class<T> bindingEntityClass) {
        this(builder.initialEnergy, builder.initBehavior, Maps.transformValues(builder.behaviors, Behavior.Builder::build), builder.onStart, builder.onEnd, builder.judge, builder.behaviorChange, builder.keyChange, builder.keys, builder.listeners, bindingEntityClass);
    }

    public Skill(int initialEnergy, @Nonnull String initBehavior, Map<String, Behavior<T>> behaviors, Consumer<SkillData<T>> onEnable,
                 Consumer<SkillData<T>> onDiable, ToBooleanBiFunction<SkillData<T>, String> judge, BiConsumer<SkillData<T>, String> stateChange, KeyInput.Consumer<T> keyChange,
                 IntList keys, Map<Class<? extends Event>, BiConsumer<? extends Event, SkillData<T>>> listeners, Class<T> bindingEntityClass) {
        if (behaviors.isEmpty()) {
            LOGGER.error("Behaviors list should hava at least one element. A empty Element is added.", new Throwable());
            this.behaviors = ImmutableMap.of("default", (Behavior<T>) Behavior.EMPTY);
        } else {
            this.behaviors = ImmutableMap.copyOf(behaviors);
        }

        Behavior<T> b = this.behaviors.get(initBehavior);
        if (b != null) {
            this.initBehavior = b;
            this.initBehaviorName = initBehavior;
        } else {
            LOGGER.error("Init behavior(name={}) not exist in behaviors({}). Changed.", initBehavior, Arrays.toString(behaviors.keySet().toArray()));
            Map.Entry<String, Behavior<T>> entry = this.behaviors.entrySet().stream().findAny().get();
            this.initBehavior = entry.getValue();
            this.initBehaviorName = entry.getKey();
        }

        this.initialEnergy = Math.clamp(0, this.initBehavior.getMaxEnergy(), initialEnergy);

        this.onEnable = onEnable;
        this.onDiable = onDiable;
        this.judge = judge;
        this.stateChange = stateChange;
        this.keyChange = keyChange;

        this.keys = IntList.of(keys.toIntArray());
        this.listeners = ImmutableMap.copyOf(listeners);
        // this.flags = ImmutableSet.copyOf(builder.flags);

        this.bindingEntityClass = bindingEntityClass;
    }

    public static class Builder<T extends Entity> {

        public final Consumer<SkillData<T>> NO_ACTION = data -> {};

        public int initialEnergy;

        @Nonnull
        public String initBehavior;

        public HashMap<String, Behavior.Builder<T>> behaviors = new HashMap<>();

        public Consumer<SkillData<T>> onStart = NO_ACTION;
        public Consumer<SkillData<T>> onEnd = NO_ACTION;
        public ToBooleanBiFunction<SkillData<T>, String> judge = (data, behavior) -> true;
        public BiConsumer<SkillData<T>, String> behaviorChange = (data, behaviorRecord) -> {};
        public KeyInput.Consumer<T> keyChange = (data, packet) -> {};
        public IntList keys = new IntArrayList();
        public HashMap<Class<? extends Event>, BiConsumer<? extends Event, SkillData<T>>> listeners = new HashMap<>();

        // public HashSet<Flag> flags = new HashSet<>();

        public Builder(@NotNull String initBehavior) {
            this.initBehavior = initBehavior;
        }

        public Builder(int initialEnergy, @NotNull String initBehavior) {
            this.initialEnergy = initialEnergy;
            this.initBehavior = initBehavior;
        }

        public static <T extends Entity> Builder<T> of(int initialEnergy, @NotNull String initBehavior) {
            return new Builder<>(initialEnergy, initBehavior);
        }

        public static <T extends Entity> Builder<T> of(Skill<T> skill) {
            Builder<T> builder = of(skill.initialEnergy, "default");
            return builder.copyFrom(skill);
        }

        public Builder<T> copyFrom(Skill<T> skill) {
            this.initialEnergy = skill.initialEnergy;

            this.initBehavior = skill.initBehaviorName;

            skill.behaviors.forEach((name, builder) -> this.behaviors.put(name, Behavior.Builder.create(builder)));

            this.onStart = skill.onEnable;
            this.onEnd = skill.onDiable;
            this.judge = skill.judge;
            this.behaviorChange = skill.stateChange;
            this.listeners.putAll(skill.listeners);
            return this;
        }

        /**
         * 当技能被设置为*启用*状态时触发
         */
        public Builder<T> start(Consumer<SkillData<T>> consumer) {
            onStart = consumer;
            return this;
        }

        public Builder<T> inactive(Consumer<Behavior.Builder<T>> inactive) {
            inactive.accept(this.behaviors.computeIfAbsent("inactive", key -> Behavior.Builder.<T>create().useInactiveCounter()));
            return this;
        }

        public Builder<T> active(Consumer<Behavior.Builder<T>> active) {
            active.accept(this.behaviors.computeIfAbsent("active", key -> Behavior.Builder.<T>create().useActiveCounter().onEnergyEmpty(data -> data.switchTo("inactive"))));
            return this;
        }

        public Builder<T> setInitialEnergy(int initialEnergy) {
            this.initialEnergy = initialEnergy;
            return this;
        }

        public Builder<T> setInitBehavior(@Nonnull String initBehavior) {
            this.initBehavior = initBehavior;
            return this;
        }

        public Builder<T> addBehavior(String name, Consumer<Behavior.Builder<T>> consumer) {
            Behavior.Builder<T> builder = Behavior.Builder.create();
            consumer.accept(builder);
            return addBehavior(name, builder);
        }

        public Builder<T> addBehavior(int maxEnergy, int maxCharge, String name, Consumer<Behavior.Builder<T>> consumer) {
            Behavior.Builder<T> builder = Behavior.Builder.create(maxEnergy, maxCharge);
            consumer.accept(builder);
            return addBehavior(name, builder);
        }

        public Builder<T> addBehavior(String name, Behavior.Builder<T> builder) {
            behaviors.put(name, builder);
            return this;
        }

        public Builder<T> removeBehavior(String name) {
            behaviors.remove(name);
            return this;
        }

        public Builder<T> removeBehavior() {
            behaviors.clear();
            return this;
        }

        /**
         * 为技能增加事件监听器<br>
         * 技能处于*启用*状态时可被监听触发
         *
         * @param clazz    需要监听的实体事件
         * @param consumer 事件行为
         * @param <E>      被监听的事件
         */
        public <E extends Event> Builder<T> onEvent(Class<E> clazz, BiConsumer<E, SkillData<T>> consumer) {
            listeners.put(clazz, consumer);
            return this;
        }

        public Builder<T> judge(ToBooleanBiFunction<SkillData<T>, String> judge) {
            this.judge = judge;
            return this;
        }

        public Builder<T> onBehaviorChange(BiConsumer<SkillData<T>, String> consumer) {
            this.behaviorChange = consumer;
            return this;
        }

        public Builder<T> onKeyInput(KeyInput.Consumer<T> consumer, int... keyListeners) {
            this.keys = new IntArrayList(keyListeners);
            this.keyChange = consumer;
            return this;
        }

        public Builder<T> apply(Consumer<Builder<T>> consumer) {
            consumer.accept(this);
            return this;
        }

        // public Builder<T> flag(Flag flag, boolean execute) {
        // return flag(flag, execute, null, null);
        // }
        //
        // public Builder<T> flag(Flag flag, boolean execute, String nameRedirect1, String nameRedirect2) {
        // flags.add(flag);
        // if (execute) {
        // flag.consumer.accept(this, nameRedirect1, nameRedirect2);
        // }
        // return this;
        // }

        public Builder<T> onEnd(Consumer<SkillData<T>> consumer) {
            onEnd = consumer;
            return this;
        }

        public Skill<T> build(Class<T> targetType) {
            return new Skill<>(this, targetType);
        }

        public Skill<T> build(Consumer<SkillData<T>> consumer, Class<T> targetType) {
            onEnd = consumer;
            return build(targetType);
        }
    }

    @Deprecated(forRemoval = true)
    public enum Flag implements StringRepresentable {

        AUTO_START("auto_start", (builder, redirectName1, redirectName2) -> builder.behaviors.get(redirectName1 == null ? "inactive" : redirectName1).onChargeReady(data -> data.switchTo(redirectName2 == null ? "active" : redirectName2))),
        AUTO_FINISH("auto_finish", (builder, redirectName1, redirectName2) -> builder.behaviors.get(Objects.requireNonNullElse(redirectName1, "active")).onEnergyEmpty(data -> data.switchTo(Objects.requireNonNullElse(redirectName2, "inactive")))),

        // INSTANT_COMPLETE("instant_complete", (builder, redirectName1, redirectName2) -> builder.maxStageEnergy = 0),
        // PASSIVITY("passivity", (builder, redirectName1, redirectName2) -> builder.energy = 0),

        INTERRUPTIBLE("interruptible"),

        TIME_ADD_INACTIVE_ENERGY("time_add_inactive_energy", (builder, redirectName1, redirectName2) -> builder.behaviors.get(redirectName1 == null ? "inactive" : redirectName1).onTick((event, data) -> data.addEnergy(1))),
        CLEAN_ACTIVE_ENERGY("clean_active_energy", (builder, redirectName1, redirectName2) -> builder.behaviors.get(Objects.requireNonNullElse(redirectName1, "active")).endWith(data -> data.setForegroundEnergy(0))),
        MARK_SKILL_TIME("mark_skill_time", (builder, redirectName1, redirectName2) -> builder.onBehaviorChange((data, toName) -> {
            if (toName.equals(Objects.requireNonNullElse(redirectName1, "active"))) data.consumerActiveStart();
        }));

        public final String name;
        public final TriConsumer<Builder<? extends Entity>, String, String> consumer;

        Flag(String name, TriConsumer<Builder<? extends Entity>, String, String> consumer) {
            this.name = name;
            this.consumer = consumer;
        }

        Flag(String name) {
            this(name, (builder, redirectName1, redirectName2) -> {});
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public ResourceLocation getId() {
        return BreaRegistries.SKILL.getKey(this);
    }

    public ResourceKey<Skill<?>> getResourceKey() {
        return BreaRegistries.SKILL.getResourceKey(this).get();
    }

    @Override
    public String toString() {
        return "BreaBlast-Skill{key=" + getId() + ", behaviors=" + Arrays.toString(behaviors.keySet().toArray()) + "}";
    }
}
