package com.phasetranscrystal.brealib.mui.widget.custom;

import com.phasetranscrystal.brealib.mui.texture.GuiTexture;
import com.phasetranscrystal.brealib.mui.widget.SlotWidget;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import net.minecraft.world.entity.player.Player;

import lombok.Getter;

public class PlayerInventoryWidget extends WidgetGroup {

    @Getter
    private GuiTexture slotBackground = SlotWidget.ITEM_SLOT_TEXTURE.copy();

    public PlayerInventoryWidget() {
        this(Position.ORIGIN);
    }

    public PlayerInventoryWidget(int x, int y) {
        this(new Position(x, y));
    }

    public PlayerInventoryWidget(Position position) {
        super(position, new Size(172, 86));

        for (int col = 0; col < 9; col++) {
            String id = "player_inv_" + col;
            var pos = new Position(5 + col * 18, 5 + 58);
            var slot = new SlotWidget();
            slot.initTemplate();
            slot.setSelfPosition(pos);
            slot.setId(id);
            addWidget(slot);
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                var id = "player_inv_" + (col + (row + 1) * 9);
                var pos = new Position(5 + col * 18, 5 + row * 18);
                var slot = new SlotWidget();
                slot.initTemplate();
                slot.setSelfPosition(pos);
                slot.setId(id);
                addWidget(slot);
            }
        }
    }

    @Override
    public void initTemplate() {}

    @Override
    public void initWidget() {
        super.initWidget();
        for (int i = 0; i < widgets.size(); i++) {
            if (widgets.get(i) instanceof SlotWidget slotWidget) {
                slotWidget.setContainerSlot(gui.entityPlayer.getInventory(), i);
                slotWidget.setLocationInfo(true, i < 9);
                slotWidget.setBackground(slotBackground);
                slotWidget.setCanPutItems(true);
                slotWidget.setCanTakeItems(true);
            }
        }
    }

    public void setPlayer(Player entityPlayer) {
        for (int i = 0; i < widgets.size(); i++) {
            if (widgets.get(i) instanceof SlotWidget slotWidget) {
                slotWidget.setContainerSlot(entityPlayer.getInventory(), i);
                slotWidget.setLocationInfo(true, i < 9);
            }
        }
    }

    public void setSlotBackground(GuiTexture slotBackground) {
        this.slotBackground = slotBackground;
        for (var widget : widgets) {
            if (widget instanceof SlotWidget slotWidget) {
                slotWidget.setBackground(slotBackground);
            }
        }
    }
}
