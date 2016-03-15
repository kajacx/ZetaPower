package com.hrkalk.zetapower.gui;

import com.hrkalk.zetapower.tileentities.ZetaChest;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ZetaChestSlot extends Slot {
    private ZetaChest chest;

    public ZetaChestSlot(ZetaChest chest, int index, int xPosition, int yPosition) {
        super(chest, index, xPosition, yPosition);
        this.chest = chest;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return chest.isItemValidForSlot(slotNumber, stack);
    }

    @Override
    public int getSlotStackLimit() {
        return chest.getInventoryStackLimit(slotNumber);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        L.i("TODO: maybe figure out what this method does?");
        return getSlotStackLimit();
    }

}
