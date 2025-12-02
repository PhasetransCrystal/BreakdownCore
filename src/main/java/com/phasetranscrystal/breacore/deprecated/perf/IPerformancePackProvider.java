package com.phasetranscrystal.breacore.deprecated.perf;

import net.minecraft.world.entity.EquipmentSlotGroup;

import java.util.Collection;

public interface IPerformancePackProvider {

    PerformancePack get(EquipmentSlotGroup slotGroup);

    boolean isStable();

    Collection<EquipmentSlotGroup> availableSlotGroups();
}
