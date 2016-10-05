package com.hrkalk.zetapower.blocks;

import com.hrkalk.zetapower.dimension.ChunksAllocator.AllocatedSpace;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.entities.vessel.VesselEntity;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.Util;
import com.hrkalk.zetapower.vessel.BlockCluster;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ModBlockShipCoreTier1_Reload {

    public ModBlockShipCoreTier1 thiz;

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote || heldItem == null || heldItem.getItem() != Items.DIAMOND) {
            return false;
        }

        int mallocDim = ZetaDimensionHandler.mallocDimension.type.getId();
        World mallocWorld = DimensionManager.getWorld(mallocDim);

        if (playerIn.dimension == mallocDim) {
            //dont spawn ships from malloc dimension
            return false;
        }

        // MEKA ACTIVATE!
        L.d("MEKA ACTIVATE!");

        AllocatedSpace space = ZetaDimensionHandler.mallocDimension.allocator.allocate12(11, 11);

        BlockPos center = new BlockPos(space.getX12() + 5, pos.getY(), space.getZ12() + 5);
        BlockPos from = center.add(-5, -5, -5);
        BlockPos to = center.add(6, 6, 6);
        //Vec3d anchor = new Vec3d(center.getX() + .5, center.getY() + .5, center.getZ() + .5);
        BlockCluster cluster = new BlockCluster(mallocWorld, from, to, space);//, anchor, space);
        //cluster.setSpace(space);

        //Util.teleportAll(worldIn, pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, DimensionManager.getWorld(mallocDim), space.getX14(), pos.getY() - 1, space.getZ14(), 3, 3, 3);
        L.d("teleporting");
        Util.teleportAll(worldIn, pos.add(-5, -5, -5), cluster);

        L.d("Spawing ship...");
        VesselEntity ship = new VesselEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), cluster);
        worldIn.spawnEntityInWorld(ship);
        L.d("Ship spawned");

        return true;
    }

}
