package com.hrkalk.zetapower.blocks;

import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockTestTeleporter_Reload {

    public BlockTestTeleporter thiz;

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            World mallocWorld = DimensionManager.getWorld(ZetaDimensionHandler.mallocDimension.type.getId());
            L.d("same: " + (world == mallocWorld));
            teleportBlock(world, pos.getX(), pos.getY(), pos.getZ() + 1, mallocWorld, pos.getX(), pos.getY(), pos.getZ() - 1);
        }


        //world.setBlockState(pos, state);
        //new BlockPos()

        //L.d("x: " + hitX + ", y: " + hitY + ", zzzDDD: " + hitZ);
        return true;
    }

    private void teleportBlock(World worldFrom, int fromX, int fromY, int fromZ, World worldTo, int toX, int toY, int toZ) {
        //L.d("teleporting 23");

        BlockPos from = new BlockPos(fromX, fromY, fromZ);
        BlockPos to = new BlockPos(toX, toY, toZ);

        TileEntity fromTe = worldFrom.getTileEntity(from);
        TileEntity toTe = worldTo.getTileEntity(to);

        IBlockState state = worldFrom.getBlockState(from);

        if (toTe != null) {
            toTe.markDirty();
            worldTo.removeTileEntity(to);
            worldTo.setBlockState(to, Blocks.AIR.getDefaultState());
        }
        worldTo.setBlockState(to, state);

        if (fromTe != null) {
            fromTe.markDirty();
            toTe = worldTo.getTileEntity(to);
            NBTTagCompound tag = fromTe.serializeNBT();
            tag.setInteger("x", to.getX());
            tag.setInteger("y", to.getY());
            tag.setInteger("z", to.getZ());
            toTe.readFromNBT(tag);
            toTe.markDirty();

            worldFrom.removeTileEntity(from);
            //Util.sendRefreshPacketFromServer(world, to);
        }

        worldFrom.setBlockState(from, Blocks.AIR.getDefaultState());

        //fromTe.setPos(to);
        //world.setTileEntity(to, fromTe);
        //world.removeTileEntity(from);

        //world.setTileEntity(to, fromTe);
        //world.removeTileEntity(from);

        //if (toTe != null) {
        //TODO: get rid of this TE
        //}
        //world.setBlockState(from, Blocks.AIR.getDefaultState());

        //if (fromTe != null) {
        //NBTTagCompound tag = fromTe.serializeNBT();

        //world.setBlockState(to, state);
        //TileEntity te = ((ITileEntityProvider) state.getBlock()).createNewTileEntity(world, state.getBlock().getMetaFromState(state));
        //TileEntity te = world.getTileEntity(to);

        //world.setTileEntity(to, te);
        //te.readFromNBT(tag);
        //world.markBlockForUpdate(toX, toX, toX);
        //te.markDirty();
        //} else {
        //   world.setBlockState(to, state);
        //}

        /*L.d("teleporting 2");
        IBlockState state = world.getBlockState(from);
        
        TileEntity te = world.getTileEntity(from);
        if (te != null) {
            world.setTileEntity(to, te);
        }
        
        try {
            EventHandlerTET.preventDropDueToTET = true;
            world.setTileEntity(from, null);
            //world.setBlockState(from, Blocks.AIR.getDefaultState());
            world.setBlockState(to, state);
        } finally {
            //EventHandlerTET.preventDropDueToTET = false;
        }*/
    }
}
