package com.hrkalk.zetapower.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Util {
    public static void sendRefreshPacketFromServer(World world, BlockPos pos) {
        if (world == null || pos == null || world.isRemote) {
            //we are on client;
            L.w("invalid parameters");
            return;
        }
        WorldServer worldServer = ((WorldServer) world);

        TileEntity te = world.getTileEntity(pos);
        if (te == null) {
            L.w("no tile entoty to refresh");
            return;
        }

        SPacketUpdateTileEntity packet = te.getUpdatePacket();
        if (packet == null) {
            L.d(" packet is null");
            return;
        }

        PlayerChunkMap map = worldServer.getPlayerChunkMap();
        PlayerChunkMapEntry entry = map.getEntry(pos.getX(), pos.getZ());
        entry.sendPacket(packet);
        L.i("sync packet successfully sent");
    }

    public static void teleportAll(World worldFrom, int fromX, int fromY, int fromZ, World worldTo, int toX, int toY, int toZ, int xSize, int ySize, int zSize) {
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < xSize; z++) {
                    teleportBlock(worldFrom, fromX + x, fromY + y, fromZ + z, worldTo, toX + x, toY + y, toZ + z);
                }
            }
        }
    }

    public static void teleportBlock(World worldFrom, int fromX, int fromY, int fromZ, World worldTo, int toX, int toY, int toZ) {

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
        }

        worldFrom.setBlockState(from, Blocks.AIR.getDefaultState());
    }
}
