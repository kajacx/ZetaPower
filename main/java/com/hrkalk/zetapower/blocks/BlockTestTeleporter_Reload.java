package com.hrkalk.zetapower.blocks;

import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.Util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
            Util.teleportBlock(world, pos.getX(), pos.getY(), pos.getZ() + 1, mallocWorld, pos.getX(), pos.getY(), pos.getZ() - 1);
        }


        //world.setBlockState(pos, state);
        //new BlockPos()

        //L.d("x: " + hitX + ", y: " + hitY + ", zzzDDD: " + hitZ);
        return true;
    }


}
