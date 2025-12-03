package com.phasetranscrystal.breacore.data.misc;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import com.phasetranscrystal.breacore.api.BreaAPI;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.api.worldgen.DimensionMarker;
import com.phasetranscrystal.brealib.utils.FormattingUtil;
import com.tterrag.registrate.util.entry.BlockEntry;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

public class BreaDimensionMarkers {

    static {
        BreaRegistries.DIMENSION_MARKERS.unfreeze(true);
        REGISTRATE.creativeModeTab(() -> null);
    }

    public static final BlockEntry<Block> OVERWORLD_MARKER = createMarker("overworld");
    public static final BlockEntry<Block> NETHER_MARKER = createMarker("the_nether");
    public static final BlockEntry<Block> END_MARKER = createMarker("the_end");

    public static final DimensionMarker OVERWORLD = createAndRegister(Level.OVERWORLD.location(), 0,
            () -> OVERWORLD_MARKER, null);
    public static final DimensionMarker NETHER = createAndRegister(Level.NETHER.location(), 0,
            () -> NETHER_MARKER, null);
    public static final DimensionMarker END = createAndRegister(Level.END.location(), 0,
            () -> END_MARKER, null);

    public static DimensionMarker createAndRegister(ResourceLocation dim, int tier, ResourceLocation itemKey,
                                                    @Nullable String overrideName) {
        DimensionMarker marker = new DimensionMarker(tier, itemKey, overrideName);
        marker.register(dim);
        return marker;
    }

    public static DimensionMarker createAndRegister(ResourceLocation dim, int tier, Supplier<ItemLike> supplier,
                                                    @Nullable String overrideName) {
        DimensionMarker marker = new DimensionMarker(tier, supplier, overrideName);
        marker.register(dim);
        return marker;
    }

    private static BlockEntry<Block> createMarker(String name) {
        return REGISTRATE.block("%s_marker".formatted(name), Block::new)
                .lang(FormattingUtil.toEnglishName(name))
                .blockstate(() -> (ctx, prov) -> {
                    var north = prov.modLoc("block/dim_markers/%s/north".formatted(name));
                    prov.getBuilder()
                            .texture(TextureSlot.DOWN, prov.modLoc("block/dim_markers/%s/down".formatted(name)))
                            .texture(TextureSlot.UP, prov.modLoc("block/dim_markers/%s/up".formatted(name)))
                            .texture(TextureSlot.NORTH, north)
                            .texture(TextureSlot.SOUTH, prov.modLoc("block/dim_markers/%s/south".formatted(name)))
                            .texture(TextureSlot.EAST, prov.modLoc("block/dim_markers/%s/east".formatted(name)))
                            .texture(TextureSlot.WEST, prov.modLoc("block/dim_markers/%s/west".formatted(name)))
                            .texture(TextureSlot.PARTICLE, north)
                            .guiLight(UnbakedModel.GuiLight.FRONT).build(ctx.get());
                })
                .simpleItem()
                .register();
    }

    public static void init() {
        BreaAPI.postRegisterEvent(BreaRegistries.DIMENSION_MARKERS);
        BreaRegistries.DIMENSION_MARKERS.freeze();
    }
}
