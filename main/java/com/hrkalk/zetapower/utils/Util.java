package com.hrkalk.zetapower.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    @SuppressWarnings("unchecked")
    public static <T> T getField(Object instance, String field) {
        if (instance == null || field == null) {
            L.w("Field " + field + " or instance" + instance + " is null");
            return null;
        }
        Class<?> clazz = instance.getClass();
        while (clazz != Object.class) {
            try {
                Field f = clazz.getDeclaredField(field);
                f.setAccessible(true);
                return (T) f.get(instance);
            } catch (NoSuchFieldException ex) {
                //field not found, move on
            } catch (Exception ex) {
                //genuine exception
                ex.printStackTrace(System.out);
                return null;
            }
        }
        L.w("Field " + field + " not found in object " + instance);
        return null;
    }

    public static Class<?>[] getClasses(Object... params) {
        Class<?>[] classes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            classes[i] = params.getClass();
        }
        return classes;
    }

    public static Object callMethod1(Object instance, String method, Object... params) {
        try {
            return callMethod(instance, method, getClasses(params), params);
        } catch (NullPointerException ex) {
            ex.printStackTrace(System.out);
            L.w("Some params are null");
            return null;
        }
    }

    public static Object callMethod(Object instance, String method, Class<?>[] paramTypes, Object[] params) {
        if (instance == null || method == null || paramTypes == null || params == null) {
            L.w("Method " + method + " or instance" + instance + " or params " + params + " is null");
            return null;
        }
        Class<?> clazz = instance.getClass();
        while (clazz != Object.class) {
            try {
                Method[] ms = clazz.getDeclaredMethods();
                if (ms.length == 1) {
                    ms[0].setAccessible(true);
                    return ms[0].invoke(instance, params);
                }

                Method m = clazz.getDeclaredMethod(method, paramTypes);
                m.setAccessible(true);
                return m.invoke(instance, params);
            } catch (NoSuchMethodException ex) {
                //field not found, move on
            } catch (Exception ex) {
                //genuine exception
                ex.printStackTrace(System.out);
                return null;
            }
        }
        L.w("Method " + method + " not found in object " + instance);
        return null;
    }

    public static <T> T construct(Class<T> clazz, Object... params) {
        try {
            Constructor<T> con = clazz.getDeclaredConstructor(getClasses(params));
            con.setAccessible(true);
            return con.newInstance(params);
        } catch (Exception ex) {
            L.w("Failed to invoke constructor");
            ex.printStackTrace(System.out);
            return null;
        }
    }
}
