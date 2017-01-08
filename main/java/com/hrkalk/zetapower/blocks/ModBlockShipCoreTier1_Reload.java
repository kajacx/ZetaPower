package com.hrkalk.zetapower.blocks;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.client.render.vessel.ScaledRotator;
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
import net.minecraft.util.math.Vec3d;
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

        int sizeX = 1; //5
        int sizeY = 1; //5
        int sizeZ = 1; //5

        AllocatedSpace space = ZetaDimensionHandler.mallocDimension.allocator.allocate12(2 * sizeX + 1, 2 * sizeZ + 1);

        BlockPos center = new BlockPos(space.getX12() + sizeX, pos.getY(), space.getZ12() + sizeZ);
        Vec3d anchor = new Vec3d(center.getX() + .5, center.getY() + .5, center.getZ() + .5);
        BlockPos from = center.add(-sizeX, -sizeY, -sizeZ);
        BlockPos to = center.add(sizeX + 1, sizeY + 1, sizeZ + 1);
        //Vec3d anchor = new Vec3d(center.getX() + .5, center.getY() + .5, center.getZ() + .5);
        BlockCluster cluster = new BlockCluster(mallocWorld, from, to, anchor);//, anchor, space);
        cluster.setSpace(space).setRotator(new ScaledRotator(new Vector3f(-1, 0, 0)));

        //Util.teleportAll(worldIn, pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, DimensionManager.getWorld(mallocDim), space.getX14(), pos.getY() - 1, space.getZ14(), 3, 3, 3);
        L.d("teleporting3");
        Util.teleportAll(worldIn, pos.add(-sizeX, -sizeY, -sizeZ), cluster);

        L.d("Spawing ship...");
        VesselEntity ship = new VesselEntity(worldIn, anchor.xCoord - center.getX() + pos.getX(), anchor.yCoord - center.getY() + pos.getY(), anchor.zCoord - center.getZ() + pos.getZ(), cluster);
        worldIn.spawnEntityInWorld(ship);
        L.d("Ship spawned");

        return true;
    }

}
