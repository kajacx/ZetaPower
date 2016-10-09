package com.hrkalk.zetapower.dimension;

import com.hrkalk.zetapower.Main;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class MallocSavedData extends WorldSavedData {
    private static final String DATA_NAME = Main.MODID + "_ExampleData";

    // Required constructors
    public MallocSavedData() {
        super(DATA_NAME);
    }

    public MallocSavedData(String s) {
        super(s);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return null;
    }
}