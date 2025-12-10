package com.phasetranscrystal.breacore.common.blast.skill;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.common.blast.BreaBlast;
import com.phasetranscrystal.breacore.common.horiz.BreaHoriz;
import com.phasetranscrystal.breacore.common.horiz.EventDistributor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SkillData<T extends Entity> {

    public static final Codec<SkillData<? extends Entity>> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("foregroundEnergy").forGetter(s -> s.foregroundEnergy),
            Codec.INT.fieldOf("backgroundEnergy").forGetter(s -> s.backgroundEnergy),
            Codec.STRING.fieldOf("behavior").forGetter(SkillData::getBehaviorName),
            Codec.BOOL.fieldOf("enabled").forGetter(SkillData::isEnabled),
            BreaRegistries.SKILL.byNameCodec().fieldOf("skill").forGetter(i -> i.skill),
            Codec.INT.fieldOf("activeTimes").forGetter(SkillData::getActiveTimes),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("cacheData").forGetter(SkillData::getCacheData),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("extendData").forGetter(SkillData::getExtendData),
            Codec.STRING.listOf().fieldOf("markCleanKeys").forGetter(i -> i.markCleanKeys.stream().toList()),
            Codec.STRING.listOf().fieldOf("markCleanCacheOnce").forGetter(i -> i.markCleanCacheOnce.stream().toList())).apply(instance, SkillData::new));
    public static final Logger LOGGER = LogManager.getLogger("BreaBlast:Skill/Data");
    public static final ResourceLocation SKILL_BASE_KEY = BreaUtil.byPath("blast/skill_base");
    public static final ResourceLocation SKILL_BEHAVIOR_KEY = BreaUtil.byPath("blast/skill_behavior");

    @Getter
    private boolean enabled = true;
    public final Skill<T> skill;
    @Getter
    private T entity;// NOSAVE
    private boolean enableAttribute = false;// NOSAVE 只在绑定的实体通过校验后变为true

    // 前景能量与背景能量在behavior的useActiveCounter切换时自动交换
    // 这一设计是为了隔离技能充能能量和技能释放能量
    @Getter
    private int foregroundEnergy;
    @Getter
    private int backgroundEnergy;
    @Getter
    private String behaviorName;
    @Getter
    private Behavior<T> behavior;

    @Getter
    private int activeTimes = 0;

    @Getter
    public final Map<String, String> cacheData = new HashMap<>();
    @Getter
    public final Map<String, String> extendData = new HashMap<>();
    @Getter
    public final Map<String, Object> extendOutsideData = new HashMap<>();
    private HashSet<String> markCleanKeys = new HashSet<>();
    private HashSet<String> markCleanCacheOnce = new HashSet<>();

    private final Set<Pair<Holder<Attribute>, ResourceLocation>> attributeCache = new HashSet<>();// NOSAVE

    private boolean markChanged = true;
    private boolean loading = false;

    public SkillData(final Skill<T> skill) {
        this.skill = skill;
        this.behaviorName = skill.initBehaviorName;
        this.behavior = skill.initBehavior;
        this.foregroundEnergy = skill.initialEnergy;
    }

    @SuppressWarnings("unchecked")
    protected SkillData(int foregroundEnergy, int backgroundEnergy, String behaviorName, boolean enabled, Skill<?> skill, int activeTimes,
                        Map<String, String> cacheData, Map<String, String> extendData, List<String> markClean, List<String> markCleanCacheOnce) {
        this.skill = (Skill<T>) skill;
        if (!skill.behaviors.containsKey(behaviorName)) {

            LOGGER.warn("Unable to find behavior({}) when load, switched to default status.", behaviorName);
            this.behaviorName = (String) skill.behaviors.keySet().toArray()[0];
            this.behavior = (Behavior<T>) skill.behaviors.get(this.behaviorName);
            this.foregroundEnergy = skill.initialEnergy;
        } else {
            this.loading = true;
            this.foregroundEnergy = foregroundEnergy;
            this.backgroundEnergy = backgroundEnergy;
            this.enabled = enabled;
            this.activeTimes = activeTimes;
            this.behaviorName = behaviorName;
            this.behavior = (Behavior<T>) skill.behaviors.get(behaviorName);
            this.cacheData.putAll(cacheData);
            this.extendData.putAll(extendData);
            this.markCleanKeys.addAll(markClean);
            this.markCleanCacheOnce.addAll(markCleanCacheOnce);
        }
    }

    // ---[实体绑定初始化]---

    @SuppressWarnings("unchecked,unused")
    public boolean tryCastAnd(LivingEntity entity, Consumer<T> consumer) {
        try {
            consumer.accept((T) entity);
            return true;
        } catch (ClassCastException e) {
            LOGGER.warn("Unable to cast an instance of {} to {}.", entity.getClass(), skill.bindingEntityClass);
            LOGGER.warn("Details:", e);
            return false;
        }
    }

    public void bindEntity(T entity) {
        if (this.entity != null && !entity.getUUID().equals(this.entity.getUUID())) {
            LOGGER.warn("Entity instance (class={}) already exists. Skipped.", entity.getClass());
            return;
        }
        if (!(entity instanceof LivingEntity living)) {
            this.enableAttribute = false;
        } else if (!living.getAttributes().hasAttribute(BreaBlast.SKILL_INACTIVE_ENERGY) ||
                !living.getAttributes().hasAttribute(BreaBlast.SKILL_ACTIVE_ENERGY) ||
                !living.getAttributes().hasAttribute(BreaBlast.SKILL_MAX_CHARGE)) {
                    LOGGER.info("Try to bind skill {} to {} with no target attribute. Dispatch Enabled.", skill.getId(), entity);
                    this.enableAttribute = false;
                } else {
                    this.enableAttribute = true;
                    // 由于我们在上面已经判断过具有属性 所以这里并不会出错
                    living.getAttribute(this.behavior.useActiveCounter ? BreaBlast.SKILL_ACTIVE_ENERGY : BreaBlast.SKILL_INACTIVE_ENERGY).setBaseValue(this.behavior.maxStageEnergy);
                    living.getAttribute(BreaBlast.SKILL_MAX_CHARGE).setBaseValue(this.behavior.maxCharge);
                }

        this.entity = entity;
        if (!enabled) return;

        enable();
    }

    @SuppressWarnings("all")
    private void enable() {
        enabled = true;

        // 技能基础行为初始化
        EventDistributor distribute = entity.getData(BreaHoriz.EVENT_DISTRIBUTOR);
        // distribute.add(EntityTickEvent.Post.class, ticker, Skill.NAME, skillName, SKILL_BASE_KEY);
        skill.onEnable.accept(this);
        skill.listeners.forEach((clazz, consumer) -> distribute.add(clazz, event -> ((BiConsumer) consumer).accept(event, this), Skill.NAME, skill.getId(), SKILL_BASE_KEY));

        markChanged = true;

        postBehavior(distribute);
    }

    // 在技能被启用或状态被切换至时执行
    @SuppressWarnings("unchecked,rawtypes")
    private void postBehavior(EventDistributor distribute) {
        markChanged = true;

        String behaviorNameCache = this.behaviorName;
        behavior.start.accept(this);
        if (!this.behaviorName.equals(behaviorNameCache)) return;

        int energy = getForegroundEnergy();
        int stageEnergy = getMaxStageEnergy();
        int maxCharge = getMaxCharge();
        int charge = getMaxCharge() == 0 ? 0 : energy / stageEnergy;

        if (charge > 0 && maxCharge > 1) {
            behavior.chargeReady.accept(this);
        }
        if (charge >= maxCharge) {
            behavior.chargeFull.accept(this);
        }
        if (energy <= 0) {
            behavior.energyEmpty.accept(this);
        }

        behavior.energyChange.accept(this, energy);
        if (charge > 0) {
            behavior.chargeChange.accept(this, charge);
        }

        behavior.listeners.forEach((clazz, consumer) -> distribute.add(clazz, event -> ((BiConsumer) consumer).accept(event, this), Skill.NAME, skill.getId(), SKILL_BEHAVIOR_KEY));

        loading = false;
    }

    @SuppressWarnings("all")
    public boolean switchToIfNot(String behavior) {
        if (!this.behaviorName.equals(behavior)) {
            return switchTo(behavior);
        }
        return false;
    }

    public boolean switchTo(String behaviorName) {
        if (!enabled) return false;
        EventDistributor eventDtb = entity.getData(BreaHoriz.EVENT_DISTRIBUTOR);

        Behavior<T> behaviorTo = skill.behaviors.get(behaviorName);

        if (behaviorTo == null) {
            LOGGER.error("Unable to find Behavior(name={}), state switch canceled. See debug.log for more details.", behaviorName);
            LOGGER.debug("Details: Skill={} Entity={uuid={}, type={}}", skill.getId(), entity.getUUID(), entity.getType());
            LOGGER.debug(new Throwable());
            return false;
        }

        if (!skill.judge.applyAsBoolean(this, behaviorName))
            return false;

        eventDtb.removeInPath(Skill.NAME, skill.getId(), SKILL_BEHAVIOR_KEY);

        this.behavior.end.accept(this);

        if (entity instanceof LivingEntity living) {
            attributeCache.forEach(pair -> living.getAttribute(pair.getFirst()).removeModifier(pair.getSecond()));
            attributeCache.clear();
        }

        markCleanKeys.forEach(cacheData::remove);
        markCleanKeys = markCleanCacheOnce;
        markCleanCacheOnce = new HashSet<>();

        skill.stateChange.accept(this, behaviorName);

        if (behaviorTo.useActiveCounter != this.behavior.useActiveCounter) {
            exchangeEnergy();
        }

        this.behaviorName = behaviorName;
        this.behavior = behaviorTo;

        if (enableAttribute) {
            LivingEntity living = (LivingEntity) entity;
            living.getAttribute(this.behavior.useActiveCounter ? BreaBlast.SKILL_ACTIVE_ENERGY : BreaBlast.SKILL_INACTIVE_ENERGY).setBaseValue(this.behavior.maxStageEnergy);
            living.getAttribute(BreaBlast.SKILL_MAX_CHARGE).setBaseValue(this.behavior.maxCharge);
        }

        postBehavior(eventDtb);

        return true;
    }

    public boolean requestEnable() {
        if (entity == null || entity.isRemoved() || enabled) return false;
        enable();
        return true;
    }

    public boolean requestDisable() {
        if (!enabled) return false;
        disable();
        return true;
    }

    @SuppressWarnings("all")
    private boolean disable() {
        EventDistributor distribute = entity.getData(BreaHoriz.EVENT_DISTRIBUTOR);
        distribute.removeInPath(Skill.NAME, skill.getId());
        skill.onDiable.accept(this);
        enabled = false;
        behaviorName = skill.initBehaviorName;
        behavior = skill.initBehavior;
        foregroundEnergy = skill.initialEnergy;
        backgroundEnergy = 0;
        activeTimes = 0;
        if (entity instanceof LivingEntity living) {
            attributeCache.forEach(pair -> living.getAttribute(pair.getFirst()).removeModifier(pair.getSecond()));
            attributeCache.clear();
            if (enableAttribute) {
                living.getAttribute(BreaBlast.SKILL_MAX_CHARGE).setBaseValue(behavior.maxCharge);
                living.getAttribute(BreaBlast.SKILL_ACTIVE_ENERGY).setBaseValue(behavior.useActiveCounter ? behavior.maxStageEnergy : 0);
                living.getAttribute(BreaBlast.SKILL_INACTIVE_ENERGY).setBaseValue(behavior.useActiveCounter ? 0 : behavior.maxStageEnergy);
            }
        }
        markCleanCacheOnce.clear();
        markCleanKeys.clear();
        cacheData.clear();
        markChanged = true;
        return true;
    }

    // ---[能力分发调度]---

    // ---[状态 State]---
    public boolean addEnergy() {
        return addEnergy(1) == 1;
    }

    // @SuppressWarnings("all")
    public int addEnergy(int amount) {
        return addEnergy(amount, false);
    }

    public int addEnergy(int amount, boolean consumeCharge) {
        return addEnergy(amount, consumeCharge, behavior.maxCharge);
    }

    public int addEnergyAllowOverCharge(int amount, boolean consumeCharge) {
        return addEnergy(amount, consumeCharge, Integer.MAX_VALUE);
    }

    // consumeCharge : 是否允许通过能量消耗使充能层数降低。若为否则能量最多消耗至当前充能层数+0能量
    public int addEnergy(int amount, boolean consumeCharge, int allowedOverCharge) {
        if (!enabled) return 0;
        if (amount == 0) return 0;

        int energy = getForegroundEnergy();
        int maxStageEnergy;
        int maxCharge;
        if (enableAttribute) {
            LivingEntity living = (LivingEntity) entity;
            maxStageEnergy = (int) living.getAttribute(this.behavior.useActiveCounter ? BreaBlast.SKILL_ACTIVE_ENERGY : BreaBlast.SKILL_INACTIVE_ENERGY).getValue();
            maxCharge = (int) living.getAttribute(BreaBlast.SKILL_MAX_CHARGE).getValue();
        } else {
            maxStageEnergy = behavior.maxStageEnergy;
            maxCharge = behavior.maxCharge;
        }

        // 计算最大可增加的能量
        int maxEnergy = allowedOverCharge == Integer.MAX_VALUE ? Integer.MAX_VALUE : (maxStageEnergy * maxCharge);
        if (amount > 0 && energy >= maxEnergy) return 0;

        int chargeCache = energy / maxStageEnergy;
        int energyCache = energy;

        energy = Math.clamp((amount > 0 || consumeCharge) ? 0 : chargeCache * maxStageEnergy, maxEnergy, energy + amount);

        int deltaEnergy = energy - energyCache;

        if (deltaEnergy == 0) return 0;

        this.foregroundEnergy += deltaEnergy;

        behavior.energyChange.accept(this, deltaEnergy);

        int chargeTo = getCharge();
        if (chargeTo != chargeCache) {
            behavior.chargeChange.accept(this, chargeTo - chargeCache);
            if (maxCharge > 1 && chargeCache <= 0 && chargeTo >= 1)
                behavior.chargeReady.accept(this);
            if (chargeTo >= maxCharge && chargeCache < maxCharge)
                behavior.chargeFull.accept(this);
        }

        markChanged = true;

        return deltaEnergy; // 如果没有增加charge，仍然返回消耗的总能量点数
    }

    public void exchangeEnergy() {
        int v = this.foregroundEnergy;
        this.foregroundEnergy = this.backgroundEnergy;
        this.backgroundEnergy = v;
    }

    // ---[缓存数据 DataCache]---

    public String getCacheData(String key) {
        return cacheData.get(key);
    }

    public int getCacheDataAsInt(String key, int fallback, boolean logIfFailed) {
        String result = cacheData.get(key);
        if (result != null) {
            try {
                return Integer.parseInt(result);
            } catch (Exception ignored) {}
        }
        if (logIfFailed) {
            String s = result == null ? "null" : ("\"" + result + "\"");
            LOGGER.error("Unable to parse String({}) to int. Fallback used. See debug.log for more details.", s);
            LOGGER.debug("Details: CacheData(key={}, value={}) to int. Skill={}, Stage={}", key, s, skill, behaviorName);
            LOGGER.debug("Fired at com.phasetranscrystal.nonard.skill.SkillData#getCacheDataAsInt", new Throwable());
        }
        return fallback;
    }

    public double getCacheDataAsDouble(String key, double fallback, boolean logIfFailed) {
        String result = cacheData.get(key);
        if (result != null) {
            try {
                return Double.parseDouble(result);
            } catch (Exception ignored) {}
        }
        if (logIfFailed) {
            String s = result == null ? "null" : ("\"" + result + "\"");
            LOGGER.error("Unable to parse String({}) to double. Fallback used. See debug.log for more details.", s);
            LOGGER.debug("Details: CacheData(key={}, value={}) to double. Skill={}, Stage={}", key, s, skill, behaviorName);
            LOGGER.debug("Fired at com.phasetranscrystal.nonard.skill.SkillData#getCacheDataAsDouble", new Throwable());
        }
        return fallback;
    }

    public String putCacheData(String key, String value, boolean markAutoClean, boolean keepToNextStage) {
        if (!enabled || entity == null) return null;
        if (markAutoClean) {
            if (keepToNextStage) markCleanCacheOnce.add(key);
            else markCleanKeys.add(key);
        }
        return cacheData.put(key, value);
    }

    // ---[额外数据 ExtendData]---
    public String putExtendData(String key, String value) {
        if (!enabled || entity == null) return null;
        return extendData.put(key, value);
    }

    public String getExtendData(String key) {
        return extendData.get(key);
    }

    // 外部额外数据例如技能升级带来的数值变动等
    // 不会被保存。建议在初始化实体之前加载。
    public void putExtendOutsideData(Map<String, Object> extendData) {
        this.extendOutsideData.putAll(extendData);
    }

    @SuppressWarnings("null")
    public boolean addAutoCleanAttribute(AttributeModifier modifier, Holder<Attribute> type) {
        AttributeInstance instance;
        if (!enabled || entity == null || !(entity instanceof LivingEntity living) || (instance = living.getAttribute(type)) == null)
            return false;

        instance.addOrUpdateTransientModifier(modifier);
        this.attributeCache.add(Pair.of(type, modifier.id()));
        return true;
    }

    // ---[数据获取 Getter]---
    public int getMaxStageEnergy() {
        return enableAttribute ? (int) ((LivingEntity) entity).getAttribute(behavior.useActiveCounter ? BreaBlast.SKILL_ACTIVE_ENERGY : BreaBlast.SKILL_INACTIVE_ENERGY).getValue() : behavior.getMaxEnergy();
    }

    public int getCharge() {
        return this.foregroundEnergy / getMaxStageEnergy();
    }

    public int getMaxCharge() {
        return enableAttribute ? (int) ((LivingEntity) entity).getAttribute(BreaBlast.SKILL_MAX_CHARGE).getValue() : behavior.maxCharge;
    }

    public void consumerActiveStart() {
        this.activeTimes++;
        // TODO 全局计数器
    }

    public void cacheOnce(String key) {
        markCleanCacheOnce.add(key);
        markCleanKeys.remove(key);
    }

    public void setForegroundEnergy(int foregroundEnergy) {
        addEnergy(foregroundEnergy - getForegroundEnergy());
    }

    public void setCharge(int charge) {
        setForegroundEnergy(getMaxStageEnergy() * charge);
    }

    public boolean consumeCharge() {
        return consumeCharge(getCharge()) == 1;
    }

    public int consumeCharge(int charge) {
        charge = Math.min(getCharge(), charge);
        addEnergy(-getMaxStageEnergy() * charge, true);
        return charge;
    }

    public void consumeChanged() {
        this.markChanged = false;
    }

    public boolean isChanged() {
        return markChanged;
    }

    // public record BehaviorRecord(String behaviorName, boolean isActive) {
    // public static final Codec<BehaviorRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    // Codec.STRING.fieldOf("name").forGetter(BehaviorRecord::behaviorName),
    // Codec.BOOL.fieldOf("active").forGetter(BehaviorRecord::isActive)
    // ).apply(instance, BehaviorRecord::new));
    // }
}
