package com.hrkalk.zetapower.dimension;

import net.minecraft.world.DimensionType;

public class MallocDimension {
    public DimensionType type;
    public ChunksAllocator allocator = new ChunksAllocator();
}
