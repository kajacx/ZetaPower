package com.hrkalk.zetapower.blocks;

import com.hrkalk.zetapower.dimension.ChunksAllocator.AllocatedSpace;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.entities.RideableShip;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.Util;
import com.hrkalk.zetapower.vessel.IterableSpace;

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

        if (playerIn.dimension == mallocDim) {
            //dont spawn ships from malloc dimension
            return false;
        }

        // MEKA ACTIVATE!
        L.d("MEKA ACTIVATE!");

        AllocatedSpace space = ZetaDimensionHandler.mallocDimension.allocator.allocate(1, 1);

        BlockPos anchor = new BlockPos(space.getX14() + 1, pos.getY(), space.getZ14() + 1);
        BlockPos from = anchor.add(-1, -1, -1);
        BlockPos to = anchor.add(2, 2, 2);
        IterableSpace iterSpace = IterableSpace.createCubeSpace(anchor, from, to);

        Util.teleportAll(worldIn, pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, DimensionManager.getWorld(mallocDim), space.getX14(), pos.getY() - 1, space.getZ14(), 3, 3, 3);

        L.d("Spawing ship...");
        RideableShip ship = new RideableShip(worldIn, pos.getX(), pos.getY(), pos.getZ(), space, iterSpace);
        worldIn.spawnEntityInWorld(ship);
        L.d("Ship spawned");

        return true;
    }

}
