package com.hrkalk.zetapower.dimension;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class MallocWorldProvider extends WorldProvider {
    protected static DimensionType type;
    public static World mallocWorld;

    @Override
    public DimensionType getDimensionType() {
        //L.i("captured malloc world: " + worldObj);
        mallocWorld = worldObj;
        return type;
    }

}
