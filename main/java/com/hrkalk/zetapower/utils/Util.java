package com.hrkalk.zetapower.utils;

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
}
