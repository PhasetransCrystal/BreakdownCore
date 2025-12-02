package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.breacore.common.quench.perk.EntityPerkHandlerAttachment;
import com.phasetranscrystal.breacore.common.quench.perk.EquipPerkComponent;
import com.phasetranscrystal.breacore.common.quench.perk.ExtraPerkWeightComponent;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.phasetranscrystal.breacore.api.registry.registry.BreaRegistrate.Brea;

public class BreaQuench {

    public static final String MODULE_ID = "quench";
    public static final String MODULE_NAME = "Quench";

    public static void bootstrap(IEventBus bus) {
    }

    public static final RegistryEntry<DataComponentType<?>, DataComponentType<EquipAssemblyComponent>> EQUIP_ASSEMBLY_COMPONENT;
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<EquipPerkComponent>> EQUIP_PERK_COMPONENT;
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<ExtraPerkWeightComponent>> EXTRA_PERK_WEIGHT_COMPONENT;

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<EntityPerkHandlerAttachment>> ENTITY_PERK_HANDLER_ATTACHMENT;

    static {
        EQUIP_ASSEMBLY_COMPONENT = Brea.simple("quench/equip_assembly",
                Registries.DATA_COMPONENT_TYPE,
                () -> DataComponentType.<EquipAssemblyComponent>builder().persistent(null).build());//TODO
        EQUIP_PERK_COMPONENT = Brea.simple("quench/equip_perk",
                Registries.DATA_COMPONENT_TYPE,
                () -> DataComponentType.<EquipPerkComponent>builder().persistent(EquipPerkComponent.CODEC).build());
        EXTRA_PERK_WEIGHT_COMPONENT = Brea.simple("quench/extra_perk_weight",
                Registries.DATA_COMPONENT_TYPE,
                () -> DataComponentType.<ExtraPerkWeightComponent>builder().persistent(ExtraPerkWeightComponent.CODEC).build());

        ENTITY_PERK_HANDLER_ATTACHMENT = Brea.simple("quench/perk_handler",
                NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                () -> AttachmentType.builder(EntityPerkHandlerAttachment::new).build());
    }
}
