package com.phasetranscrystal.breacore.api.item.debug;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import com.phasetranscrystal.breacore.test.TestMenu;
import org.jetbrains.annotations.NotNull;

public class MuiItem extends Item {

    public MuiItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        var oi = player.openMenu(new SimpleMenuProvider((id, inv, pla) -> new TestMenu(id), Component.translatable("title.breacore.mui_item")));
        return InteractionResult.SUCCESS;
    }
}
