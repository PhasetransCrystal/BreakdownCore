package com.phasetranscrystal.breacore.common.quench.perk;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.attribute.TriNum;
import com.phasetranscrystal.breacore.common.horiz.BreaHoriz;
import com.phasetranscrystal.breacore.common.horiz.EventDistributor;
import com.phasetranscrystal.breacore.common.quench.BreaQuench;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import org.joml.Math;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static com.phasetranscrystal.breacore.common.quench.BreaQuench.ENTITY_PERK_HANDLER_ATTACHMENT;

/**
 * 实体词条强度处理器
 * <p>
 * 缓存实体所有装备槽位对应的{@link EquipPerkComponent 装备词条组件}数据。
 * 在装备切换时更新玩家当前生效的perk总集，并根据状态自动附加/清除属性与事件监听。
 * <p>
 * 所有词条产生的属性变动会被以统一的id附加给实体。
 * <p>
 * 不同装备间的同词条强度取高，且强度值在[0,最大强度]之间。
 */
@EventBusSubscriber
public class EntityPerkHandlerAttachment {

    // 预设的一系列用于处理attribute与event distribute的id
    public static final ResourceLocation EVENT_ROOT = BreaUtil.byPath("perk_events");
    public static final ResourceLocation MODIFIER_ROOT = BreaUtil.byPath("perk_attr_modifier");
    public static final ResourceLocation MODIFIER_S1 = MODIFIER_ROOT.withSuffix("/stage1");
    public static final ResourceLocation MODIFIER_S2 = MODIFIER_ROOT.withSuffix("/stage2");
    public static final ResourceLocation MODIFIER_S3 = MODIFIER_ROOT.withSuffix("/stage3");

    /**
     * 每个槽位的词条数据组件缓存，用于在更改后对比与收集以生成新的数据
     */
    private final Map<EquipmentSlot, EquipPerkComponent> perksBySlot = new HashMap<>();
    /**
     * 缓存生效的各词条与其强度系数
     */
    private Map<Perk, Double> perksAndStrength = new HashMap<>();
    /**
     * 缓存所有生效词条产出的属性数据
     */
    private Map<Holder<Attribute>, TriNum.Mutable> attributes = new HashMap<>();
    /**
     * 标记是否需要更新
     */
    private boolean isDirty;

    /**
     * 更新数据。考虑到可能存在原装备有词条组件信息而新装备没有，不会先行检查新装备组件以短路逻辑。
     *
     * @param slot      发生变动的装备槽位
     * @param component 变动后的词条组件信息
     */
    public void update(EquipmentSlot slot, EquipPerkComponent component) {
        // 如果没有任何变动 不更新内容
        if (Objects.equals(perksBySlot.get(slot), component)) return;

        // 设置为需要更新状态
        this.isDirty = true;
        if (component != null) {
            perksBySlot.put(slot, component);
        } else {
            perksBySlot.remove(slot);
        }
    }

    /**
     * 检查是否需要更新，并更新逻辑
     */
    public void tick(LivingEntity living) {
        if (!isDirty) return;
        isDirty = false;

        // 收集所有装备上的词条数据，取最大值
        HashMap<Perk, Double> npas = new HashMap<>();
        perksBySlot.values().forEach(comp -> comp.enabledPerks().forEach((perk, strength) -> {
            npas.put(perk, Math.max(npas.getOrDefault(perk, 0D), strength));
            // npas.put(perk, Math.clamp(0, perk.getMaxPerkStrength(), Math.max(npas.getOrDefault(perk, 0D),
            // strength)));
        }));

        // 计算新添加与移除的词条，用于处理事件系统
        EventDistributor distributor = living.getData(BreaHoriz.EVENT_DISTRIBUTOR.get());
        // 移除内容
        perksAndStrength.keySet().stream()
                .filter(p -> !npas.containsKey(p))
                .forEach(perk -> distributor.removeInPath(EVENT_ROOT, perk.getId()));
        // 新增内容
        npas.keySet().stream()
                .filter(p -> !perksAndStrength.containsKey(p))
                .forEach(perk -> perk.getEventConsumers().forEach((event, consumer) -> {
                    distributor.add(event, e -> consumer.accept(e, this.getPerkStrength(perk)), EVENT_ROOT, perk.getId());
                }));

        // 将新数据覆盖旧数据
        perksAndStrength = npas;

        // 计算属性总和
        Map<Holder<Attribute>, TriNum.Mutable> na = new HashMap<>();
        // 根据词条强度获取Attribute
        npas.forEach((perk, strength) -> perk.getAttributesByStrength(strength).forEach((atr, tri) -> {
            na.computeIfAbsent(atr, a -> new TriNum.Mutable()).add(tri);
        }));

        // 收集所有涉及到的属性
        HashSet<Holder<Attribute>> allAttr = new HashSet<>(na.keySet());
        allAttr.addAll(attributes.keySet());

        allAttr.forEach(holder -> {
            AttributeInstance instance = living.getAttribute(holder);
            if (instance != null) {
                if ((na.containsKey(holder) && !attributes.containsKey(holder)) || // 新增属性
                        (na.containsKey(holder) && attributes.containsKey(holder) && !na.get(holder).equals(attributes.get(holder)))) {// 属性变化
                    // 移除旧有属性修饰器
                    instance.removeModifier(MODIFIER_S1);
                    instance.removeModifier(MODIFIER_S2);
                    instance.removeModifier(MODIFIER_S3);
                    // 根据新数值添加新的属性修饰器
                    TriNum.Mutable num = na.get(holder);
                    if (num.getV1() != 0)
                        instance.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_S1, num.getV1(), AttributeModifier.Operation.ADD_VALUE));
                    if (num.getV2() != 0)
                        instance.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_S2, num.getV2(), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    if (num.getV3() != 1)
                        instance.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_S3, num.getV3() - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                } else if (!na.containsKey(holder) && attributes.containsKey(holder)) {// 消去属性
                    // 移除属性
                    instance.removeModifier(MODIFIER_S1);
                    instance.removeModifier(MODIFIER_S2);
                    instance.removeModifier(MODIFIER_S3);
                }
            }
        });

        attributes = na;
    }

    public double getPerkStrength(Perk perk) {
        return perksAndStrength.get(perk);
    }

    /**
     * 监听实体装备事件，处理实体的装备变化
     */
    @SubscribeEvent
    public static void entityEquip(LivingEquipmentChangeEvent event) {
        event.getEntity().getData(ENTITY_PERK_HANDLER_ATTACHMENT).update(event.getSlot(), event.getTo().get(BreaQuench.EQUIP_PERK_COMPONENT));
    }

    /**
     * 在实体tick时更新词条变动
     */
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living) {
            event.getEntity().getExistingData(ENTITY_PERK_HANDLER_ATTACHMENT).ifPresent(handler -> handler.tick(living));
        }
    }
}
