package com.hrkalk.zetapower.dimension;

import com.hrkalk.zetapower.utils.L;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class ZetaDimensionHandler {
    public static MallocDimension mallocDimension;

    public static void initDimensions() {
        int mallocId = DimensionManager.getNextFreeDimId();
        DimensionType mallocDim = DimensionType.register("Malloc", "_malloc", mallocId, MallocWorldProvider.class, true);
        MallocWorldProvider.type = mallocDim;
        DimensionManager.registerDimension(mallocId, mallocDim);
        mallocDimension = new MallocDimension();
        mallocDimension.type = mallocDim;

        L.i("Setting malloc dimension to: " + mallocDimension);
        L.i("Malloc dimension registered with id: " + mallocId);
    }
}
