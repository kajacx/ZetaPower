package com.hrkalk.zetapower.client.render.vessel;

import com.hrkalk.zetapower.vessel.BlockCluster;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

public class ClusterAccess implements IBlockAccess {
    private BlockCluster cluster;

    public ClusterAccess(BlockCluster cluster) {
        this.cluster = cluster;
    }

    public boolean contains(BlockPos pos) {
        BlockPos from = cluster.getFrom();
        if (pos.getX() < from.getX() || pos.getY() < from.getY() || pos.getZ() < from.getZ()) {
            return false;
        }
        BlockPos to = cluster.getTo();
        if (pos.getX() >= to.getX() || pos.getY() >= to.getY() || pos.getZ() >= to.getZ()) {
            return false;
        }
        return true;
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return contains(pos) ? cluster.getWorld().getTileEntity(pos) : null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return contains(pos) ? cluster.getWorld().getCombinedLight(pos, lightValue) : 200;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return contains(pos) ? cluster.getWorld().getBlockState(pos) : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return contains(pos) ? cluster.getWorld().isAirBlock(pos) : true;
    }

    @Override
    public Biome getBiomeGenForCoords(BlockPos pos) {
        return cluster.getWorld().getBiomeGenForCoords(pos); //no idea what this does or what to return
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return contains(pos) ? cluster.getWorld().getStrongPower(pos, direction) : 0;
    }

    @Override
    public WorldType getWorldType() {
        return cluster.getWorld().getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return contains(pos) ? cluster.getWorld().isSideSolid(pos, side, _default) : _default;
    }

}
