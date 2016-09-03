package com.hrkalk.zetapower.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class SimpleTeleporter extends Teleporter {

    protected WorldServer world;

    public SimpleTeleporter(WorldServer worldIn) {
        super(worldIn);
        this.world = worldIn;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        int x = (int) entityIn.posX;
        int y = (int) entityIn.posY + 5;
        int z = (int) entityIn.posZ;

        world.setBlockState(new BlockPos(x, y, z), Blocks.DIRT.getDefaultState());
    }

}
