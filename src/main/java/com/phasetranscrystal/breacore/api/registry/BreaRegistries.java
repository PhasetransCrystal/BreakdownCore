package com.phasetranscrystal.breacore.api.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.serialization.MapCodec;
import com.phasetranscrystal.breacore.api.material.Element;
import com.phasetranscrystal.breacore.api.material.Material;
import com.phasetranscrystal.breacore.api.material.registry.MaterialRegistry;
import com.phasetranscrystal.breacore.api.sound.SoundEntry;
import com.phasetranscrystal.breacore.api.worldgen.DimensionMarker;
import com.phasetranscrystal.breacore.common.horiz.SavableEventConsumerData;
import com.phasetranscrystal.breacore.common.quench.perk.Perk;
import com.phasetranscrystal.breacore.common.quench.stuct.EquipType;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

public class BreaRegistries {

    public static final ResourceLocation ROOT_REGISTRY_NAME = BreaUtil.byPath("root");
    public static final BreaRegistry<BreaRegistry<?>> ROOT = new BreaRegistry<>(ROOT_REGISTRY_NAME);
    // TODO ResourceKey

    public static <T> ResourceKey<Registry<T>> makeRegistryKey(ResourceLocation registryId) {
        return ResourceKey.createRegistryKey(registryId);
    }

    private static final Table<Registry<?>, ResourceLocation, Object> TO_REGISTER = HashBasedTable.create();

    public static <V, T extends V> T register(Registry<V> registry, ResourceLocation name, T value) {
        TO_REGISTER.put(registry, name, value);
        return value;
    }

    // ignore the generics and hope the registered objects are still correctly typed :3
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void actuallyRegister(RegisterEvent event) {
        for (Registry reg : TO_REGISTER.rowKeySet()) {
            event.register(reg.key(), helper -> {
                TO_REGISTER.row(reg).forEach(helper::register);
            });
        }
    }

    public static void init(IEventBus eventBus) {
        Consumer<RegisterEvent> actuallyRegister = BreaRegistries::actuallyRegister;
        eventBus.addListener(actuallyRegister);
    }

    private static final RegistryAccess BLANK = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    private static RegistryAccess FROZEN = BLANK;

    /**
     * You shouldn't call it, you should probably not even look at it just to be extra safe
     *
     * @param registryAccess the new value to set to the frozen registry access
     */
    @ApiStatus.Internal
    public static void updateFrozenRegistry(RegistryAccess registryAccess) {
        FROZEN = registryAccess;
    }

    public static RegistryAccess builtinRegistry() {
        if (FROZEN == BLANK && BreaUtil.isClientThread()) {
            if (Minecraft.getInstance().getConnection() != null) {
                return Minecraft.getInstance().getConnection().registryAccess();
            }
        }
        return FROZEN;
    }

    public static final ResourceKey<Registry<MapCodec<? extends SavableEventConsumerData<?>>>> SAVABLE_EVENT_CONSUMER_TYPE_KEY = makeRegistryKey(BreaUtil.byPath("horiz/savable_event_consumer"));
    public static final BreaRegistry<MapCodec<? extends SavableEventConsumerData<?>>> SAVABLE_EVENT_CONSUMER_TYPE = new BreaRegistry<>(SAVABLE_EVENT_CONSUMER_TYPE_KEY);

    public static final ResourceKey<Registry<EquipType>> EQUIP_TYPE_KEY = makeRegistryKey(BreaUtil.byPath("quench/equip_type"));
    public static final ResourceKey<Registry<Perk>> PERK_KEY = makeRegistryKey(BreaUtil.byPath("quench/perk"));
    public static final BreaRegistry<EquipType> EQUIP_TYPE = new BreaRegistry<>(EQUIP_TYPE_KEY);
    public static final BreaRegistry<Perk> PERK = new BreaRegistry<>(PERK_KEY);

    public static final ResourceKey<Registry<Material>> MATERIAL_KEY = makeRegistryKey(BreaUtil.byPath("material"));
    public static final ResourceKey<Registry<Element>> ELEMENT_KEY = makeRegistryKey(BreaUtil.byPath("element"));
    public static final BreaRegistry<Element> ELEMENTS = new BreaRegistry<>(ELEMENT_KEY);
    public static final MaterialRegistry MATERIALS = new MaterialRegistry(MATERIAL_KEY);

    public static final ResourceKey<Registry<SoundEntry>> SOUND_KEY = makeRegistryKey(BreaUtil.byPath("sound"));
    public static final BreaRegistry<SoundEntry> SOUNDS = new BreaRegistry<>(SOUND_KEY);

    public static final ResourceKey<Registry<DimensionMarker>> DIMENSION_MARKER_REGISTRY = makeRegistryKey(BreaUtil.byPath("dimension_marker"));
    public static final BreaRegistry<DimensionMarker> DIMENSION_MARKERS = new BreaRegistry<>(DIMENSION_MARKER_REGISTRY);
}
