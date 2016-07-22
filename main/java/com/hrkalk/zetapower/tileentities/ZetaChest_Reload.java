package com.hrkalk.zetapower.tileentities;

import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.NBTReader;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;

public class ZetaChest_Reload {

    public ZetaChest thiz;

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        thiz.super_writeToNBT(compound);

        L.d("Write to nbt");

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < thiz.getSizeInventory(); ++i) {
            if (thiz.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                thiz.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items", list);

        L.d("Has custom name: " + thiz.hasCustomName());
        if (thiz.hasCustomName()) {
            L.d("Setting custom name to: " + thiz.getCustomName());
            compound.setString("CustomName", thiz.getCustomName());
        }

        L.d("Setting facing to:" + thiz.facing.getHorizontalIndex());
        compound.setInteger("Facing", thiz.facing.getHorizontalIndex());

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        thiz.super_readFromNBT(compound);

        L.d("read from nbt");

        NBTTagList list = NBTReader.readListOr(compound, "Items", new NBTTagList(), NBTReader.TYPE_COMPOUND);//compound.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            thiz.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        thiz.setCustomName(NBTReader.readStringOr(compound, "CustomName", null));

        int facingInt = compound.getInteger("Facing");
        L.d("Facing int: " + facingInt);

        EnumFacing facing = EnumFacing.getHorizontal(facingInt);
        L.d("Facing: " + facing);

        thiz.setEnumFacing(facing);
        L.d("Facing thiz: " + thiz.getEnumFacing());

        //thiz.facing = EnumFacing.getHorizontal(NBTReader.readIntOr(compound, "Facing", 0));
        //L.d("reading facing as: " + compound.getInteger("Facing"));

        //L.d("now facing: " + thiz.getEnumFacing());
    }

}
