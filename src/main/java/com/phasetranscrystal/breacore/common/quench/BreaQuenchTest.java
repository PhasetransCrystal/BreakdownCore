package com.phasetranscrystal.breacore.common.quench;

import com.phasetranscrystal.breacore.api.attribute.TriNum;
import com.phasetranscrystal.breacore.api.registry.BreaRegistries;
import com.phasetranscrystal.breacore.common.quench.perk.EquipPerkComponent;
import com.phasetranscrystal.breacore.common.quench.perk.Perk;
import com.phasetranscrystal.breacore.common.quench.stuct.EquipType;
import com.phasetranscrystal.brealib.utils.BreaUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BreaQuenchTest {
    public static void bootstrapConsumer(@NotNull IEventBus bus) {
        bus.addListener(BreaQuenchTest::bindingEvent);
    }

    public static class TestSwordEquipType extends EquipType {
//        public static final

        @Override
        public Map<ResourceLocation, EquipAssemblySlot<?>> getSlots() {
            return Map.of();
        }
    }


    public static class TestPerk extends Perk {

        @Override
        public double getMaxPerkStrength() {
            return 1;
        }

        @Override
        public Map<Holder<Attribute>, TriNum> getAttributesByStrength(double strength) {
            return Map.of(Attributes.JUMP_STRENGTH, new TriNum(1, 0, strength));
        }

        @Override
        public Map<Class<? extends Event>, BiConsumer<Event, Double>> getEventConsumers() {
            return Map.of(PlayerInteractEvent.RightClickBlock.class, (e, v) -> {
                PlayerInteractEvent.RightClickBlock event = (PlayerInteractEvent.RightClickBlock) e;//由于左右手 这里会触发两次
                event.getEntity().displayClientMessage(Component.literal("you right clicked a block"), false);
            }, PlayerInteractEvent.LeftClickBlock.class, (e, v) -> {
                PlayerInteractEvent.LeftClickBlock event = (PlayerInteractEvent.LeftClickBlock) e;
                if (event.getHand() == InteractionHand.MAIN_HAND) {
                    EquipPerkComponent.update(event.getItemStack());
                }
            });
        }

        @Override
        public int getPerkWeight() {
            return 0;
        }

        @Override
        public boolean forceEnable() {
            return false;
        }
    }

    public static final TestPerk TEST_PERK;

    static {
        BreaRegistries.PERK.unfreeze(false);
        TEST_PERK = BreaRegistries.PERK.register(BreaUtil.byPath("quench/test"), new TestPerk());
        BreaRegistries.PERK.freeze();
    }

    public static void bindingEvent(ModifyDefaultComponentsEvent event) {
        event.modify(Items.DIAMOND_SWORD, builder -> builder.set(
                BreaQuench.EQUIP_PERK_COMPONENT.get(), EquipPerkComponent.create(20, Map.of(TEST_PERK, 2D), List.of(TEST_PERK))
        ));
    }
}
