package com.hrkalk.zetapower.tileentities;

import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ZetaChest extends TileEntity implements ITickable, IInventory {

    private DynamicReloader reloader = new DynamicReloader(ZetaChest.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.tileentities.ZetaChest.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(20));

        reloader.addToBlacklist("net.minecraft.nbt.NBTTagCompound");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("net.minecraft.block.Block");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("net.minecraft.world.World");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("net.minecraft.tileentity.TileEntity");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("net.minecraft.util.math.BlockPos");
        reloader.addToBlacklist("com.hrkalk.zetapower.tileentities.ZetaChest");
    }

    public World get_worldObj() {
        return worldObj;
    }

    public void set_worldObj(World worldObj) {
        this.worldObj = worldObj;
    }

    public BlockPos get_pos() {
        return pos;
    }

    public void set_pos(BlockPos pos) {
        this.pos = pos;
    }

    public boolean get_tileEntityInvalid() {
        return tileEntityInvalid;
    }

    public void set_tileEntityInvalid(boolean tileEntityInvalid) {
        this.tileEntityInvalid = tileEntityInvalid;
    }

    public Block get_blockType() {
        return blockType;
    }

    public void set_blockType(Block blockType) {
        this.blockType = blockType;
    }

    public void call_func_190201_b(World arg1) {
        func_190201_b(arg1);
    }

    public NBTTagCompound super_writeToNBT(NBTTagCompound arg1) {
        return super.writeToNBT(arg1);
    }

    public void super_readFromNBT(NBTTagCompound arg1) {
        super.readFromNBT(arg1);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound arg1) {
        try {
            return (NBTTagCompound) ReflectUtil.invoke("writeToNBT", reloader.getInstance(this), arg1);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            return null;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound arg1) {
        try {
            ReflectUtil.invoke("readFromNBT", reloader.getInstance(this), arg1);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound syncData = writeToNBT(new NBTTagCompound());
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    public ItemStack[] inventory;
    public String customName;
    public EnumFacing facing;
    public int[] limit; //limit of items in each inventory
    public Item[] validItems; //certain item or null for any

    public ZetaChest() {
        inventory = new ItemStack[getSizeInventory()];
        limit = new int[getSizeInventory()];
        validItems = new Item[getSizeInventory()];

        //test
        for (int i = 0; i < getSizeInventory(); i++) {
            limit[i] = i + 10;
        }

        validItems[0] = Items.DIAMOND;
        validItems[1] = Items.CARROT;

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
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
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
