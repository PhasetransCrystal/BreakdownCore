package com.phasetranscrystal.breacore.common.quench.perk;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.phasetranscrystal.breacore.api.attribute.TriNum;
import com.phasetranscrystal.breacore.common.horiz.BreaHoriz;
import com.phasetranscrystal.breacore.common.horiz.EventDistributor;
import com.phasetranscrystal.breacore.common.quench.BreaQuench;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import org.joml.Math;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static com.phasetranscrystal.breacore.common.quench.BreaQuench.ENTITY_PERK_HANDLER_ATTACHMENT;

@EventBusSubscriber
public class EntityPerkHandlerAttachment {
    public static final ResourceLocation EVENT_ROOT = BreaUtil.byPath("perk_events");
    public static final ResourceLocation MODIFIER_ROOT = BreaUtil.byPath("perk_attr_modifier");
    public static final ResourceLocation MODIFIER_S1 = MODIFIER_ROOT.withSuffix("/stage1");
    public static final ResourceLocation MODIFIER_S2 = MODIFIER_ROOT.withSuffix("/stage2");
    public static final ResourceLocation MODIFIER_S3 = MODIFIER_ROOT.withSuffix("/stage3");

    private final Map<EquipmentSlot, EquipPerkComponent> perksBySlot = new HashMap<>();
    private Map<Perk, Double> perksAndStrength = new HashMap<>();
    private Map<Holder<Attribute>, TriNum.Mutable> attributes = new HashMap<>();
    private boolean isDirty;

    public void update(EquipmentSlot slot, EquipPerkComponent component) {
        //如果没有任何变动 不更新内容
        if (Objects.equals(perksBySlot.get(slot), component)) return;

        this.isDirty = true;
        perksBySlot.put(slot, component);
    }

    public void tick(LivingEntity living) {
        if (!isDirty) return;
        isDirty = false;

        HashMap<Perk, Double> npas = new HashMap<>();
        perksBySlot.values().forEach(comp -> comp.enabledPerks().forEach((perk, strength) -> {
            npas.merge(perk, strength, (v1, v2) -> Math.clamp(0, perk.getMaxPerkStrength(), Math.max(v1, v2)));
        }));

        HashSet<Perk> added = new HashSet<>(npas.keySet());
        HashSet<Perk> removed = new HashSet<>(perksAndStrength.keySet());

        //npas有而perksAndStrength没有
        added.removeAll(perksAndStrength.keySet());
        //perksAndStrength有而npas没有
        removed.removeAll(npas.keySet());

        EventDistributor distributor = living.getData(BreaHoriz.EVENT_DISTRIBUTOR.get());
        removed.forEach(perk -> distributor.removeInPath(EVENT_ROOT, perk.getId()));
        added.forEach(perk -> perk.getEventConsumers().forEach((event, consumer) -> {
            distributor.add(event, e -> consumer.accept(e, perksAndStrength.get(perk)));
        }));

        perksAndStrength = npas;

        Map<Holder<Attribute>, TriNum.Mutable> na = new HashMap<>();
        //根据词条强度获取Attribute
        npas.forEach((perk, strength) -> perk.getAttributesByStrength(strength).forEach((atr, tri) -> {
            na.computeIfAbsent(atr, a -> new TriNum.Mutable()).add(tri);
        }));

        HashSet<Holder<Attribute>> allAttr = new HashSet<>(na.keySet());
        allAttr.addAll(attributes.keySet());

        allAttr.forEach(holder -> {
            AttributeInstance instance = living.getAttribute(holder);
            if (instance != null) {
                if ((na.containsKey(holder) && !attributes.containsKey(holder)) || //新增属性
                        (na.containsKey(holder) && attributes.containsKey(holder) && !na.get(holder).equals(attributes.get(holder)))) {//属性变化
                    instance.removeModifier(MODIFIER_S1);
                    instance.removeModifier(MODIFIER_S2);
                    instance.removeModifier(MODIFIER_S3);
                    TriNum num = na.get(holder).build();
                    if (num.v1() != 0)
                        instance.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_S1, num.v1(), AttributeModifier.Operation.ADD_VALUE));
                    if (num.v2() != 0)
                        instance.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_S1, num.v2(), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    if (num.v3() != 1)
                        instance.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_S1, num.v3(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                } else if (!na.containsKey(holder) && attributes.containsKey(holder)) {//消去属性
                    instance.removeModifier(MODIFIER_S1);
                    instance.removeModifier(MODIFIER_S2);
                    instance.removeModifier(MODIFIER_S3);
                }
            }
        });

        attributes = na;
    }

    @SubscribeEvent
    public static void entityEquip(LivingEquipmentChangeEvent event) {
        EquipPerkComponent perkComponent = event.getTo().get(BreaQuench.EQUIP_PERK_COMPONENT);
        if (perkComponent == null) return;
        event.getEntity().getData(ENTITY_PERK_HANDLER_ATTACHMENT).update(event.getSlot(), perkComponent);
    }
}
