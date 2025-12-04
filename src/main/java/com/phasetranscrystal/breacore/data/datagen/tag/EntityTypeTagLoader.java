package com.phasetranscrystal.breacore.data.datagen.tag;

import com.phasetranscrystal.breacore.data.tags.CustomTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

public class EntityTypeTagLoader {

    public static void init(RegistrateTagsProvider.Intrinsic<EntityType<?>> provider) {
        create(provider, CustomTags.HEAT_IMMUNE, EntityType.BLAZE, EntityType.MAGMA_CUBE, EntityType.WITHER_SKELETON,
                EntityType.WITHER);
        create(provider, CustomTags.CHEMICAL_IMMUNE, EntityType.SKELETON, EntityType.STRAY);
        create(provider, CustomTags.IRON_GOLEMS, EntityType.IRON_GOLEM);
        create(provider, CustomTags.SPIDERS, EntityType.SPIDER, EntityType.CAVE_SPIDER);
    }

    public static void create(RegistrateTagsProvider.Intrinsic<EntityType<?>> provider, TagKey<EntityType<?>> tagKey,
                              EntityType<?>... rls) {
        var builder = provider.tag(tagKey);
        for (EntityType<?> entityType : rls) {
            builder.add(entityType);
        }
    }
}
