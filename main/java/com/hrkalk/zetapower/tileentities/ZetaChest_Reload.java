package com.hrkalk.zetapower.tileentities;

import com.hrkalk.zetapower.utils.MathUtils;
import com.hrkalk.zetapower.utils.NBTReader;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

public class ZetaChest_Reload {

    public ZetaChest thiz;

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        thiz.super_writeToNBT(compound);

        String location = thiz.getWorld().isRemote ? "Client" : "Server";
        //L.d(" -- " + location + " WRITE TO NBT -- ");

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < thiz.getSizeInventory(); ++i) {
            //if (i == 4)
            //    continue;
            if (thiz.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                thiz.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        /*if (thiz.getStackInSlot(3) != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stackTag.setByte("Slot", (byte) 4);
            thiz.getStackInSlot(3).writeToNBT(stackTag);
            list.appendTag(stackTag);
        }*/
        compound.setTag("Items", list);

        //L.d("Has custom name: " + thiz.hasCustomName());
        if (thiz.hasCustomName()) {
            //L.d("Setting custom name to: " + thiz.getCustomName());
            compound.setString("CustomName", thiz.getCustomName());
        }

        //L.d(location + " write facing: " + thiz.facing.getHorizontalIndex());
        compound.setInteger("Facing", thiz.facing.getHorizontalIndex());

        int rngCode = MathUtils.random.nextInt(1_000_000);
        //L.d(location + " write rng code to: " + rngCode);
        compound.setInteger("RandomInt", rngCode);

        /* try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("kappa.text"));
            out.writeObject(compound);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }*/

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        thiz.super_readFromNBT(compound);

        String location = thiz.getWorld() == null ? "Unknown" : (thiz.getWorld().isRemote ? "Client" : "Server");
        //L.d(" -- " + location + " READ FROM NBT -- ");

        NBTTagList list = NBTReader.readListOr(compound, "Items", new NBTTagList(), NBTReader.TYPE_COMPOUND);//compound.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            thiz.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        thiz.setCustomName(NBTReader.readStringOr(compound, "CustomName", null));

        int facingInt = compound.getInteger("Facing");
        //L.d(location + " read Facing int: " + facingInt);

        EnumFacing facing = EnumFacing.getHorizontal(facingInt);
        //L.d(location + " read Facing: " + facing);

        thiz.setEnumFacing(facing);
        //L.d(location + " read Facing thiz: " + thiz.getEnumFacing());

        //L.d(location + " read Rng code: " + NBTReader.readIntOr(compound, "RandomInt", -42));
        //L.d(location + " read Raw rng: " + compound.getInteger("RandomInt"));

        //thiz.facing = EnumFacing.getHorizontal(NBTReader.readIntOr(compound, "Facing", 0));
        //L.d("reading facing as: " + compound.getInteger("Facing"));

        //L.d("now facing: " + thiz.getEnumFacing());
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        String location = thiz.getWorld() == null ? "Unknown" : (thiz.getWorld().isRemote ? "Client" : "Server");
        //L.d(" -- " + location + " GET UPDATE PACKET -- ");

        NBTTagCompound syncData = writeToNBT(new NBTTagCompound());
        return new SPacketUpdateTileEntity(thiz.get_pos(), thiz.getBlockMetadata(), syncData);
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        String location = thiz.getWorld() == null ? "Unknown" : (thiz.getWorld().isRemote ? "Client" : "Server");
        //L.d(" -- " + location + " ON DATA PACKET -- ");

        readFromNBT(pkt.getNbtCompound());
    }

}
