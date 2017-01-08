package com.hrkalk.zetapower.utils;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class NBTReader {
    private static List<String> typesAsList = Arrays.asList(NBTBase.NBT_TYPES);

    public static int TYPE_BYTE = typesAsList.indexOf("BYTE");
    public static int TYPE_SHORT = typesAsList.indexOf("SHORT");
    public static int TYPE_INT = typesAsList.indexOf("INT");
    public static int TYPE_LONG = typesAsList.indexOf("LONG");
    public static int TYPE_FLOAT = typesAsList.indexOf("FLOAT");
    public static int TYPE_DOUBLE = typesAsList.indexOf("DOUBLE");
    public static int TYPE_BYTE_ARR = typesAsList.indexOf("BYTE[]");
    public static int TYPE_STRING = typesAsList.indexOf("STRING");
    public static int TYPE_LIST = typesAsList.indexOf("LIST");
    public static int TYPE_COMPOUND = typesAsList.indexOf("COMPOUND");
    public static int TYPE_INT_ARR = typesAsList.indexOf("INT[]");

    public static int readIntOr(NBTTagCompound tag, String name, int orDefault) {
        if (tag == null)
            return orDefault;
        if (!tag.hasKey(name, TYPE_INT))
            return orDefault;
        return tag.getInteger(name);
    }

    public static float readFloatOr(NBTTagCompound tag, String name, float orDefault) {
        if (tag == null)
            return orDefault;
        if (!tag.hasKey(name, TYPE_FLOAT))
            return orDefault;
        return tag.getFloat(name);
    }

    public static double readDoubleOr(NBTTagCompound tag, String name, double orDefault) {
        if (tag == null)
            return orDefault;
        if (!tag.hasKey(name, TYPE_DOUBLE))
            return orDefault;
        return tag.getDouble(name);
    }

    public static String readStringOr(NBTTagCompound tag, String name, String orDefault) {
        if (tag == null)
            return orDefault;
        if (!tag.hasKey(name, TYPE_STRING))
            return orDefault;
        return tag.getString(name);
    }

    public static NBTTagCompound readTagOr(NBTTagCompound tag, String name, NBTTagCompound orDefault) {
        if (tag == null)
            return orDefault;
        if (!tag.hasKey(name, TYPE_COMPOUND))
            return orDefault;
        return tag.getCompoundTag(name);
    }

    public static NBTTagList readListOr(NBTTagCompound tag, String name, NBTTagList orDefault, int listType) {
        if (tag == null)
            return orDefault;
        if (!tag.hasKey(name, TYPE_LIST))
            return orDefault;
        return tag.getTagList(name, listType);
    }

    public static NBTTagCompound writeBLockPosWithPrefix(NBTTagCompound tag, BlockPos pos, String prefix) {
        tag.setInteger(prefix + "X", pos.getX());
        tag.setInteger(prefix + "Y", pos.getY());
        tag.setInteger(prefix + "Z", pos.getZ());
        return tag;
    }

    public static BlockPos readBlockPosWithPrefix(NBTTagCompound tag, String prefix) {
        int x = NBTReader.readIntOr(tag, prefix + "X", 0);
        int y = NBTReader.readIntOr(tag, prefix + "Y", 0);
        int z = NBTReader.readIntOr(tag, prefix + "Z", 0);
        return new BlockPos(x, y, z);
    }

    public static NBTTagCompound writeVec3fWithPrefix(NBTTagCompound tag, Vector3f vec3f, String prefix) {
        tag.setFloat(prefix + "X", vec3f.getX());
        tag.setFloat(prefix + "Y", vec3f.getY());
        tag.setFloat(prefix + "Z", vec3f.getZ());
        return tag;
    }

    public static Vector3f readVec3fWithPrefix(NBTTagCompound tag, String prefix) {
        float x = NBTReader.readFloatOr(tag, prefix + "X", 0);
        float y = NBTReader.readFloatOr(tag, prefix + "Y", 0);
        float z = NBTReader.readFloatOr(tag, prefix + "Z", 0);
        return new Vector3f(x, y, z);
    }
}
