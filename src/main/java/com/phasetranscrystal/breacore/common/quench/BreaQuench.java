package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.common.quench.perk.EntityPerkHandlerAttachment;
import com.phasetranscrystal.breacore.common.quench.perk.EquipPerkComponent;
import com.phasetranscrystal.breacore.common.quench.perk.ExtraPerkWeightComponent;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

public class BreaQuench {

    public static final String MODULE_ID = "quench";
    public static final String MODULE_NAME = "Quench";

    public static void bootstrap() {
        if (BreaUtil.isDev()) {
            BreaQuenchTest.bootstrapConsumer();
        }
    }

    public static final RegistryEntry<DataComponentType<?>, DataComponentType<EquipAssemblyComponent>> EQUIP_ASSEMBLY_COMPONENT;
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<EquipPerkComponent>> EQUIP_PERK_COMPONENT;
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<ExtraPerkWeightComponent>> EXTRA_PERK_WEIGHT_COMPONENT;

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<EntityPerkHandlerAttachment>> ENTITY_PERK_HANDLER_ATTACHMENT;

    static {
        EQUIP_ASSEMBLY_COMPONENT = REGISTRATE.simple("quench/equip_assembly",
                Registries.DATA_COMPONENT_TYPE,
                () -> DataComponentType.<EquipAssemblyComponent>builder().persistent(EquipAssemblyComponent.CODEC).build());
        EQUIP_PERK_COMPONENT = REGISTRATE.simple("quench/equip_perk",
                Registries.DATA_COMPONENT_TYPE,
                () -> DataComponentType.<EquipPerkComponent>builder().persistent(EquipPerkComponent.CODEC).build());
        EXTRA_PERK_WEIGHT_COMPONENT = REGISTRATE.simple("quench/extra_perk_weight",
                Registries.DATA_COMPONENT_TYPE,
                () -> DataComponentType.<ExtraPerkWeightComponent>builder().persistent(ExtraPerkWeightComponent.CODEC).build());

        ENTITY_PERK_HANDLER_ATTACHMENT = REGISTRATE.simple("quench/perk_handler",
                NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                () -> AttachmentType.builder(EntityPerkHandlerAttachment::new).build());
    }
}
