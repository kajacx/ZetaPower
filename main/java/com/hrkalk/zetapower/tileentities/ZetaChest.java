package com.hrkalk.zetapower.tileentities;

import com.hrkalk.zetapower.utils.L;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

public class ZetaChest extends TileEntity implements ITickable, IInventory {
    private ItemStack[] inventory;
    private String customName;
    private EnumFacing facing;
    private int[] limit; //limit of items in each inventory
    private Item[] validItems; //certain item or null for any

    public ZetaChest() {
        inventory = new ItemStack[getSizeInventory()];
        limit = new int[getSizeInventory()];
        validItems = new Item[getSizeInventory()];

        //test
        for (int i = 0; i < getSizeInventory(); i++) {
            limit[i] = i + 10;
        }

        validItems[0] = Items.diamond;
        validItems[1] = Items.carrot;

        //legit
        for (int i = 0; i < getSizeInventory(); i++) {
            if (validItems[i] != null) {
                inventory[i] = new ItemStack(validItems[i], 0);
            }
        }

        L.d("Testing trace...");
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            //we are on server

        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items", list);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.getCustomName());
        }

        compound.setInteger("Facing", facing.getHorizontalIndex());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList list = compound.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (compound.hasKey("CustomName", 8)) {
            this.setCustomName(compound.getString("CustomName"));
        }

        try {
            facing = EnumFacing.getHorizontal(compound.getInteger("Facing"));
        } catch (Exception ex) {
            L.e(ex);
        }
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 0, nbttagcompound);
    };

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(pkt.getPos());
    }

    public EnumFacing getEnumFacing() {
        return facing;
    }

    public void setEnumFacing(EnumFacing facing) {
        this.facing = facing;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.zeta_chest";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != null) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= this.getSizeInventory()) {
            L.w("Invalid index: " + index);
            return;
        }

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            L.w("Reducing stack size when inserting into an inventory");
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (stack != null && stack.stackSize == 0)
            stack = null;

        if (stack == null && validItems[index] != null) {
            stack = new ItemStack(validItems[index], 0);
        }

        this.inventory[index] = stack;
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public int getInventoryStackLimit(int index) {
        if (index < 0 || index >= getSizeInventory()) {
            L.w("Invalid index: " + index);
            return getInventoryStackLimit();
        }
        return limit[index];
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (stack == null) {
            L.w("stack is null at index: " + index);
            return true;
        }

        if (index < 0 || index >= getSizeInventory()) {
            L.w("invalid index: " + index);
            return false;
        }

        //work-around to fix broken hopper mechanics
        if (itemsInSlot(index) >= getInventoryStackLimit(index))
            return false;

        return validItems[index] == null || validItems[index] == stack.getItem();
    }

    public int itemsInSlot(int index) {
        if (index < 0 || index >= getSizeInventory())
            return 0;
        if (inventory[index] == null)
            return 0;

        return inventory[index].stackSize;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, null);
    }
}