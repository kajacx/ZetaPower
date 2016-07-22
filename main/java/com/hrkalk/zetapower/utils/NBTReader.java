package com.hrkalk.zetapower.utils;

import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
        if (!tag.hasKey(name, TYPE_DOUBLE))
            return orDefault;
        return tag.getInteger(name);
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

    public static NBTTagList readListOr(NBTTagCompound tag, String name, NBTTagList orDefault, int listType) {
        if (tag == null)
            return orDefault;
        if (!tag.hasKey(name, TYPE_LIST))
            return orDefault;
        return tag.getTagList(name, listType);
    }
}
