package com.phasetranscrystal.brealib.mui.widget;

import com.phasetranscrystal.brealib.BreaLib;
import com.phasetranscrystal.brealib.mui.modular.ModularUI;
import com.phasetranscrystal.brealib.mui.texture.GuiTexture;
import com.phasetranscrystal.brealib.utils.Position;
import com.phasetranscrystal.brealib.utils.Size;

import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.ImageView;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.items.IItemHandlerModifiable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Accessors(chain = true)
public class SlotWidget extends Widget {

    public static final GuiTexture ITEM_SLOT_TEXTURE = new GuiTexture(ResourceLocation.fromNamespaceAndPath(BreaLib.MOD_ID, "gui/base/item_slot.png"));

    @Nullable
    protected static Slot HOVER_SLOT = null;
    @Nullable
    protected Slot slotReference;
    @Setter
    protected boolean canTakeItems;
    @Setter
    protected boolean canPutItems;
    public boolean isPlayerContainer;
    public boolean isPlayerHotBar;
    @Setter
    public boolean drawHoverOverlay = true;
    @Setter
    public boolean drawHoverTips = true;
    @Setter
    protected Runnable changeListener;
    @Setter
    protected BiConsumer<SlotWidget, List<Component>> onAddedTooltips;
    @Setter
    protected Function<ItemStack, ItemStack> itemHook;
    @Setter
    @Getter
    protected float XEIChance = 1f;
    @Getter
    private ItemStack lastItem = ItemStack.EMPTY;

    public SlotWidget() {
        super(new Position(0, 0), new Size(18, 18));
    }

    @Override
    public void initTemplate() {
        setBackgroundTexture(ITEM_SLOT_TEXTURE);
        this.canTakeItems = true;
        this.canPutItems = true;
    }

    public SlotWidget(Container inventory, int slotIndex, int xPosition, int yPosition, boolean canTakeItems, boolean canPutItems) {
        super(new Position(xPosition, yPosition), new Size(18, 18));
        setBackgroundTexture(ITEM_SLOT_TEXTURE);
        this.canTakeItems = canTakeItems;
        this.canPutItems = canPutItems;
        setContainerSlot(inventory, slotIndex);
    }

    public SlotWidget(IItemHandlerModifiable itemHandler, int slotIndex, int xPosition, int yPosition, boolean canTakeItems, boolean canPutItems) {
        super(new Position(xPosition, yPosition), new Size(18, 18));
        setBackgroundTexture(ITEM_SLOT_TEXTURE);
        this.canTakeItems = canTakeItems;
        this.canPutItems = canPutItems;
        setHandlerSlot(itemHandler, slotIndex);
    }

    protected Slot createSlot(Container inventory, int index) {
        return new WidgetSlot(inventory, index, 0, 0);
    }

    protected Slot createSlot(IItemHandlerModifiable itemHandler, int index) {
        return new WidgetSlotItemTransfer(itemHandler, index, 0, 0);
    }

    public SlotWidget setContainerSlot(Container inventory, int slotIndex) {
        updateSlot(createSlot(inventory, slotIndex));
        return this;
    }

    public SlotWidget setHandlerSlot(IItemHandlerModifiable itemHandler, int slotIndex) {
        updateSlot(createSlot(itemHandler, slotIndex));
        return this;
    }

    protected void updateSlot(Slot slot) {
        if (this.slotReference != null && this.gui != null && !isClientSideWidget) {
            getGui().removeNativeSlot(this.slotReference);
        }
        this.slotReference = slot;
        if (this.gui != null && !isClientSideWidget) {
            getGui().addNativeSlot(this.slotReference, this);
        }
    }

    public ItemStack getItem() {
        return slotReference == null ? ItemStack.EMPTY : slotReference.getItem();
    }

    public void setItem(ItemStack stack) {
        if (slotReference != null) {
            slotReference.set(stack);
        }
    }

    public void setItem(ItemStack stack, boolean notify) {
        if (slotReference != null) {
            var lastListener = changeListener;
            if (!notify) {
                changeListener = null;
            }
            slotReference.set(stack);
            changeListener = lastListener;
        }
    }

    @Override
    public final void setSize(Size size) {
        // you cant modify size.
    }

    @Override
    public void setGui(ModularUI gui) {
        if (!isClientSideWidget && this.gui != gui) {
            if (this.gui != null && slotReference != null) {
                this.gui.removeNativeSlot(slotReference);
            }
            if (gui != null && slotReference != null) {
                gui.addNativeSlot(slotReference, this);
            }
        }
        super.setGui(gui);
    }

    public SlotWidget(IItemHandlerModifiable itemHandler, int slotIndex, int xPosition, int yPosition) {
        this(itemHandler, slotIndex, xPosition, yPosition, true, true);
    }

    public SlotWidget(Container inventory, int slotIndex, int xPosition, int yPosition) {
        this(inventory, slotIndex, xPosition, yPosition, true, true);
    }

    public SlotWidget setBackgroundTexture(GuiTexture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    public boolean canPutStack(ItemStack stack) {
        return isEnabled() && canPutItems;
    }

    public boolean canTakeStack(Player player) {
        return isEnabled() && canTakeItems;
    }

    public boolean isEnabled() {
        return this.isActive() && isVisible();
    }

    public boolean canMergeSlot(ItemStack stack) {
        return isEnabled();
    }

    public void onSlotChanged() {
        if (gui == null) return;
        gui.holder.markAsDirty();
    }

    public ItemStack slotClick(int dragType, ClickType clickTypeIn, Player player) {
        return null;
    }

    @Nullable
    public final Slot getHandler() {
        return slotReference;
    }

    public SlotWidget setLocationInfo(boolean isPlayerContainer, boolean isPlayerHotBar) {
        this.isPlayerHotBar = isPlayerHotBar;
        this.isPlayerContainer = isPlayerContainer;
        return this;
    }

    public List<Component> getAdditionalToolTips(List<Component> list) {
        if (this.onAddedTooltips != null) {
            this.onAddedTooltips.accept(this, list);
        }
        return list;
    }

    protected class WidgetSlot extends Slot {

        public WidgetSlot(Container inventory, int index, int xPosition, int yPosition) {
            super(inventory, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return SlotWidget.this.canPutStack(stack) && super.mayPlace(stack);
        }

        @Override
        public boolean mayPickup(@Nonnull Player playerIn) {
            return SlotWidget.this.canTakeStack(playerIn) && super.mayPickup(playerIn);
        }

        @Override
        public void set(@Nonnull ItemStack stack) {
            // if(!SlotWidget.this.canPutStack(stack)) return;
            super.set(stack);
        }

        @Override
        public void setChanged() {
            if (changeListener != null) {
                changeListener.run();
            }
            SlotWidget.this.onSlotChanged();
        }

        @Override
        public boolean isActive() {
            return SlotWidget.this.isEnabled() && (HOVER_SLOT == null || HOVER_SLOT == this);
        }
    }

    public class WidgetSlotItemTransfer extends Slot {

        private static final Container emptyInventory = new SimpleContainer(0);
        @Getter
        private final IItemHandlerModifiable itemHandler;
        private final int index;

        public WidgetSlotItemTransfer(IItemHandlerModifiable itemHandler, int index, int xPosition, int yPosition) {
            super(emptyInventory, index, xPosition, yPosition);
            this.itemHandler = itemHandler;
            this.index = index;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return SlotWidget.this.canPutStack(stack) && (!stack.isEmpty() && this.itemHandler.isItemValid(this.index, stack));
        }

        @Override
        public boolean mayPickup(@Nullable Player playerIn) {
            return SlotWidget.this.canTakeStack(playerIn) && !this.itemHandler.extractItem(index, 1, true).isEmpty();
        }

        @Override
        @Nonnull
        public ItemStack getItem() {
            return this.itemHandler.getStackInSlot(index);
        }

        @Override
        public void set(@Nonnull ItemStack stack) {
            this.itemHandler.setStackInSlot(index, stack);
            this.setChanged();
        }

        @Override
        public void onQuickCraft(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {}

        @Override
        public int getMaxStackSize() {
            return this.itemHandler.getSlotLimit(this.index);
        }

        @Override
        public int getMaxStackSize(@Nonnull ItemStack stack) {
            ItemStack maxAdd = stack.copy();
            int maxInput = stack.getMaxStackSize();
            maxAdd.setCount(maxInput);
            ItemStack currentStack = this.itemHandler.getStackInSlot(index);
            this.itemHandler.setStackInSlot(index, ItemStack.EMPTY);
            ItemStack remainder = this.itemHandler.insertItem(index, maxAdd, true);
            this.itemHandler.setStackInSlot(index, currentStack);
            return maxInput - remainder.getCount();
        }

        @NotNull
        @Override
        public ItemStack remove(int amount) {
            var result = this.itemHandler.extractItem(index, amount, false);
            if (changeListener != null && !getItem().isEmpty()) {
                changeListener.run();
            }
            return result;
        }

        @Override
        public void setChanged() {
            if (changeListener != null) {
                changeListener.run();
            }
            SlotWidget.this.onSlotChanged();
        }

        @Override
        public boolean isActive() {
            return SlotWidget.this.isEnabled() && (HOVER_SLOT == null || HOVER_SLOT == this);
        }
    }

    @Override
    public void modifyView(ViewGroup container) {
        var iv = new ImageView(container.getContext());
        iv.setImage(backgroundTexture.getTexture().get());
        container.addView(iv);
    }
}
