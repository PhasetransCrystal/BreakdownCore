package com.phasetranscrystal.breacore.deprecated.perf;

import com.google.common.collect.*;
import com.phasetranscrystal.breacore.api.attribute.TriNum;
import com.phasetranscrystal.breacore.common.horiz.BreaHoriz;
import com.phasetranscrystal.breacore.common.horiz.EventDistributor;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public record PerformancePack(ResourceLocation[] eventPath,
                              ResourceLocation combinePath,
                              @Nullable Map<Holder<Attribute>, TriNum> attribute,
                              @Nullable Map<ResourceLocation, TriNum> equipAttribute,
                              @Nullable Map<Class<Event>, Consumer<Event>> listeners,  //TO ENTITY
                              IntSet elementHashes
) {

    public PerformancePack(ResourceLocation[] eventPath,
                           @Nullable Map<Holder<Attribute>, TriNum> attribute,
                           @Nullable Map<ResourceLocation, TriNum> equipAttribute,
                           @Nullable Map<Class<Event>, Consumer<Event>> listeners,
                           IntSet elementHashes) {
        this(eventPath, combine(eventPath), attribute, equipAttribute, listeners, elementHashes);
    }

    //附加实体事件部分
    public void binding(Entity entity) {
        if (this.listeners != null) {
            EventDistributor distribute = entity.getData(BreaHoriz.EVENT_DISTRIBUTOR.get());

            listeners.forEach((event, consumer) -> addEventListener(event, consumer, distribute));
        }
    }

    private <T extends Event> void addEventListener(Class<T> clazz, Consumer<? extends Event> consumer, EventDistributor distribute) {
        distribute.add(clazz, (Consumer<T>) consumer, eventPath);
    }

    //附加属性部分
    public List<Pair<Holder<Attribute>, AttributeModifier>> createAttributes() {
        if (this.attribute != null) {
            List<Pair<Holder<Attribute>, AttributeModifier>> list = new ArrayList<>();
            this.attribute.forEach((atr, tri) -> {
                if (tri.v1() != 0)
                    list.add(Pair.of(atr, new AttributeModifier(combinePath.withSuffix("/stage1"), tri.v1(), AttributeModifier.Operation.ADD_VALUE)));
                if (tri.v2() != 0)
                    list.add(Pair.of(atr, new AttributeModifier(combinePath.withSuffix("/stage2"), tri.v2(), AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
                if (tri.v3() != 1)
                    list.add(Pair.of(atr, new AttributeModifier(combinePath.withSuffix("/stage3"), tri.v3(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));
            });
            return list;
        }
        return List.of();
    }

    public void debind(Entity entity) {
        if (this.listeners != null) {
            EventDistributor distribute = entity.getData(BreaHoriz.EVENT_DISTRIBUTOR.get());
            distribute.removeInPath(eventPath);
        }
    }

    public static class GroupBuilder {
        public final ResourceLocation[] path;
        private final List<PerformancePack> children = new ArrayList<>();

        public GroupBuilder(ResourceLocation[] path) {
            this.path = path;
        }

        public GroupBuilder addChild(PerformancePack child) {
            children.add(child);
            return this;
        }

        public GroupBuilder addChildren(Collection<PerformancePack> children) {
            this.children.addAll(children);
            return this;
        }

        public PerformancePack build() {
            Map<Holder<Attribute>, TriNum.Mutable> attribute = new HashMap<>();
            Map<ResourceLocation, TriNum.Mutable> equipAttribute = new HashMap<>();
            Multimap<Class<Event>, Consumer<Event>> events = HashMultimap.create();
            IntSet elementHashes = new IntOpenHashSet();

            for (PerformancePack child : children) {
                if (child.attribute != null) {
                    child.attribute.forEach((key, value) -> {
                        attribute.computeIfAbsent(key, k -> new TriNum.Mutable()).add(value);
                    });
                }

                if (child.equipAttribute != null) {
                    child.equipAttribute.forEach((key, value) -> {
                        equipAttribute.computeIfAbsent(key, k -> new TriNum.Mutable()).add(value);
                    });
                }

                if (child.listeners != null) {
                    child.listeners.forEach(events::put);
                }
                elementHashes.add(child.hashCode());
            }


            return new PerformancePack(path,
                    buildTriNum(attribute),
                    buildTriNum(equipAttribute),
                    buildEvents(events),
                    IntSets.unmodifiable(elementHashes));
        }

        public static PerformancePack of(ResourceLocation[] path, Collection<PerformancePack> children) {
            return new GroupBuilder(path).addChildren(children).build();
        }
    }

    public static class SingleBuilder {
        public final ResourceLocation[] path;
        private final List<PerformancePack> children = new ArrayList<>();
        private Map<Holder<Attribute>, TriNum.Mutable> attribute = new HashMap<>();
        private Map<ResourceLocation, TriNum.Mutable> equipAttribute = new HashMap<>();
        private Multimap<Class<Event>, Consumer<Event>> listeners = HashMultimap.create();

        public SingleBuilder(ResourceLocation[] path) {
            this.path = path;
        }

        public SingleBuilder() {
            this.path = null;
        }

        public SingleBuilder attribute(Holder<Attribute> holder, double v1, double v2, double v3) {
            attribute.computeIfAbsent(holder, v -> new TriNum.Mutable()).add(v1, v2, v3);
            return this;
        }

        public SingleBuilder attribute(Holder<Attribute> holder, AttributeModifier.Operation op, double value) {
            attribute.computeIfAbsent(holder, v -> new TriNum.Mutable()).add(op, value);
            return this;
        }

        public SingleBuilder attributeAdd(Holder<Attribute> holder, double value) {
            return attribute(holder, AttributeModifier.Operation.ADD_VALUE, value);
        }

        public SingleBuilder attributeAddMulBase(Holder<Attribute> holder, double value) {
            return attribute(holder, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, value);
        }

        public SingleBuilder attributeAddMulTotal(Holder<Attribute> holder, double value) {
            return attribute(holder, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, value);
        }

        public SingleBuilder equipAttribute(ResourceLocation rl, double v1, double v2, double v3) {
            equipAttribute.computeIfAbsent(rl, v -> new TriNum.Mutable()).add(v1, v2, v3);
            return this;
        }

        public SingleBuilder equipAttribute(ResourceLocation rl, AttributeModifier.Operation op, double value) {
            equipAttribute.computeIfAbsent(rl, v -> new TriNum.Mutable()).add(op, value);
            return this;
        }

        public SingleBuilder equipAttributeAdd(ResourceLocation rl, double value) {
            return equipAttribute(rl, AttributeModifier.Operation.ADD_VALUE, value);
        }

        public SingleBuilder equipAttributeAddMulBase(ResourceLocation rl, double value) {
            return equipAttribute(rl, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, value);
        }

        public SingleBuilder equipAttributeAddMulTotal(ResourceLocation rl, double value) {
            return equipAttribute(rl, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, value);
        }

        public <T extends Event> SingleBuilder addListener(Class<T> eventClass, Consumer<T> consumer) {
            listeners.put((Class<Event>) eventClass, (Consumer<Event>) consumer);
            return this;
        }

        public PerformancePack build() {
            return new PerformancePack(path, buildTriNum(attribute), buildTriNum(equipAttribute), buildEvents(listeners), IntSet.of());
        }
    }




    public static Map<Class<Event>, Consumer<Event>> buildEvents(Multimap<Class<Event>, Consumer<Event>> listeners) {
        ImmutableMap.Builder<Class<Event>, Consumer<Event>> lc = new ImmutableMap.Builder<>();
        listeners.keySet().forEach(key -> {
            lc.put(key, e -> List.copyOf(listeners.get(key)).forEach(c -> c.accept(e)));
        });
        return lc.build();
    }

    public static <T> Map<T, TriNum> buildTriNum(Map<T, TriNum.Mutable> origin) {
        if (origin.isEmpty()) return null;
        ImmutableMap.Builder<T, TriNum> builder = ImmutableMap.builder();
        origin.forEach((key, value) -> builder.put(key, value.build()));
        return builder.build();
    }

    public static ResourceLocation combine(ResourceLocation[] rls) {
        if (rls.length == 0) return ResourceLocation.fromNamespaceAndPath("minecraft", "empty");
        else if (rls.length == 1) return rls[0];
        else {
            ResourceLocation rl = rls[0];
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < rls.length; i++) {
                builder.append("/").append(rls[i].getNamespace()).append("/").append(rls[i].getPath());
            }
            return rl.withSuffix(builder.toString());
        }
    }

    public static ResourceLocation[] eventPath(ResourceLocation[] root, ResourceLocation... path) {
        if (path.length == 0) return root;
        ResourceLocation[] result = new ResourceLocation[root.length + path.length];
        System.arraycopy(root, 0, result, 0, root.length);
        System.arraycopy(path, 0, result, root.length, path.length);
        return result;
    }

    public static final ResourceLocation EQUIP_ROOT = rl("equipment");
    public static final ResourceLocation STABLE = rl("stable");
    public static final ResourceLocation ACTIVE = rl("active");

    //SLOTPOS IS_STABLE PATH
    public static final Table<EquipmentSlotGroup, Boolean, ResourceLocation[]> PATH_BY_SLOT = HashBasedTable.create();

    static {
        PATH_BY_SLOT.put(EquipmentSlotGroup.ANY, true, of(EQUIP_ROOT, rl("any"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.ANY, false, of(EQUIP_ROOT, rl("any"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.MAINHAND, true, of(EQUIP_ROOT, rl("mainhand"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.MAINHAND, false, of(EQUIP_ROOT, rl("mainhand"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.OFFHAND, true, of(EQUIP_ROOT, rl("offhand"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.OFFHAND, false, of(EQUIP_ROOT, rl("offhand"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.HAND, true, of(EQUIP_ROOT, rl("hand"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.HAND, false, of(EQUIP_ROOT, rl("hand"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.FEET, true, of(EQUIP_ROOT, rl("feet"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.FEET, false, of(EQUIP_ROOT, rl("feet"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.LEGS, true, of(EQUIP_ROOT, rl("legs"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.LEGS, false, of(EQUIP_ROOT, rl("legs"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.CHEST, true, of(EQUIP_ROOT, rl("chest"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.CHEST, false, of(EQUIP_ROOT, rl("chest"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.HEAD, true, of(EQUIP_ROOT, rl("head"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.HEAD, false, of(EQUIP_ROOT, rl("head"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.ARMOR, true, of(EQUIP_ROOT, rl("armor"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.ARMOR, false, of(EQUIP_ROOT, rl("armor"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.BODY, true, of(EQUIP_ROOT, rl("body"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.BODY, false, of(EQUIP_ROOT, rl("body"), ACTIVE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.SADDLE, true, of(EQUIP_ROOT, rl("saddle"), STABLE));
        PATH_BY_SLOT.put(EquipmentSlotGroup.SADDLE, false, of(EQUIP_ROOT, rl("saddle"), ACTIVE));
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    private static ResourceLocation[] of(ResourceLocation... path) {
        return path;
    }
}
