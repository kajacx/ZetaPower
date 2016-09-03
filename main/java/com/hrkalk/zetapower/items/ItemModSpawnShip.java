package com.hrkalk.zetapower.items;

import java.util.Random;
import java.util.UUID;

import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemModSpawnShip extends BasicItem {

    private DynamicReloader reloader = new DynamicReloader(ItemModSpawnShip.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.items.ItemModSpawnShip.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(20));

        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("net.minecraft.world.World");
        reloader.addToBlacklist("net.minecraft.item.Item");
        reloader.addToBlacklist("net.minecraft.util.EnumHand");
        reloader.addToBlacklist("net.minecraft.util.EnumFacing");
        reloader.addToBlacklist("net.minecraft.util.math.BlockPos");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("java.util.Random");
        reloader.addToBlacklist("com.hrkalk.zetapower.items.ItemModSpawnShip");
        reloader.addToBlacklist("java.util.UUID");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("net.minecraft.item.ItemStack");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("net.minecraft.util.math.RayTraceResult");
        reloader.addToBlacklist("net.minecraft.block.Block");
        reloader.addToBlacklist("net.minecraft.entity.player.EntityPlayer");
        reloader.addToBlacklist("net.minecraft.util.EnumActionResult");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("com.hrkalk.zetapower.items.BasicItem");

        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.dimension");
    }

    public ItemModSpawnShip(String arg1) {
        super(arg1);
    }

    public UUID get_ATTACK_DAMAGE_MODIFIER() {
        return ATTACK_DAMAGE_MODIFIER;
    }

    public UUID get_ATTACK_SPEED_MODIFIER() {
        return ATTACK_SPEED_MODIFIER;
    }

    public Random get_itemRand() {
        return itemRand;
    }

    public static void set_itemRand(Random itemRand) {
        BasicItem.itemRand = itemRand;
    }

    public int get_maxStackSize() {
        return maxStackSize;
    }

    public void set_maxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public boolean get_bFull3D() {
        return bFull3D;
    }

    public void set_bFull3D(boolean bFull3D) {
        this.bFull3D = bFull3D;
    }

    public boolean get_hasSubtypes() {
        return hasSubtypes;
    }

    public void set_hasSubtypes(boolean hasSubtypes) {
        this.hasSubtypes = hasSubtypes;
    }

    public boolean get_canRepair() {
        return canRepair;
    }

    public void set_canRepair(boolean canRepair) {
        this.canRepair = canRepair;
    }

    public void call_registerItemBlock(Block arg1, Item arg2) {
        registerItemBlock(arg1, arg2);
    }

    public RayTraceResult call_rayTrace(World arg1, EntityPlayer arg2, boolean arg3) {
        return rayTrace(arg1, arg2, arg3);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack arg1, EntityPlayer arg2, World arg3, BlockPos arg4, EnumHand arg5, EnumFacing arg6, float arg7, float arg8, float arg9) {
        try {
            return (EnumActionResult) ReflectUtil.invoke("onItemUse", reloader.getInstance(this), arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            return null;
        }
    }

}
