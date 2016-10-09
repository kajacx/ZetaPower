package com.hrkalk.zetapower.blocks;

import java.util.List;
import java.util.Random;

import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ModBlockShipCoreTier1 extends BasicBlock {

    private DynamicReloader reloader = new DynamicReloader(ModBlockShipCoreTier1.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.blocks.ModBlockShipCoreTier1.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(20));

        reloader.addToBlacklist("net.minecraft.util.math.AxisAlignedBB");
        reloader.addToBlacklist("net.minecraft.util.EnumHand");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("net.minecraft.block.state.BlockStateContainer");
        reloader.addToBlacklist("java.util.Random");
        reloader.addToBlacklist("net.minecraft.item.ItemStack");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("net.minecraft.util.math.RayTraceResult");
        reloader.addToBlacklist("net.minecraft.block.Block");
        reloader.addToBlacklist("net.minecraft.util.math.Vec3d");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("com.hrkalk.zetapower.blocks.BasicBlock");
        reloader.addToBlacklist("net.minecraft.block.SoundType");
        reloader.addToBlacklist("net.minecraft.block.material.MapColor");
        reloader.addToBlacklist("java.lang.ThreadLocal");
        reloader.addToBlacklist("com.hrkalk.zetapower.blocks.ModBlockShipCoreTier1");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("net.minecraft.world.World");
        reloader.addToBlacklist("net.minecraft.util.EnumFacing");
        reloader.addToBlacklist("net.minecraft.util.math.BlockPos");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("net.minecraft.block.state.IBlockState");
        reloader.addToBlacklist("java.util.List");
        reloader.addToBlacklist("net.minecraft.block.material.Material");
        reloader.addToBlacklist("net.minecraft.entity.player.EntityPlayer");
        reloader.addToBlacklist("com.hrkalk.zetapower.entities.vessel.VesselEntity");

        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.dimension");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.vessel");
    }

    public ModBlockShipCoreTier1(String arg1, Material arg2, float arg3, float arg4) {
        super(arg1, arg2, arg3, arg4);
    }

    public ModBlockShipCoreTier1(String arg1, float arg2, float arg3) {
        super(arg1, arg2, arg3);
    }

    public ModBlockShipCoreTier1(String arg1) {
        super(arg1);
    }

    public boolean get_fullBlock() {
        return fullBlock;
    }

    public void set_fullBlock(boolean fullBlock) {
        this.fullBlock = fullBlock;
    }

    public int get_lightOpacity() {
        return lightOpacity;
    }

    public void set_lightOpacity(int lightOpacity) {
        this.lightOpacity = lightOpacity;
    }

    public boolean get_translucent() {
        return translucent;
    }

    public void set_translucent(boolean translucent) {
        this.translucent = translucent;
    }

    public int get_lightValue() {
        return lightValue;
    }

    public void set_lightValue(int lightValue) {
        this.lightValue = lightValue;
    }

    public boolean get_useNeighborBrightness() {
        return useNeighborBrightness;
    }

    public void set_useNeighborBrightness(boolean useNeighborBrightness) {
        this.useNeighborBrightness = useNeighborBrightness;
    }

    public float get_blockHardness() {
        return blockHardness;
    }

    public void set_blockHardness(float blockHardness) {
        this.blockHardness = blockHardness;
    }

    public float get_blockResistance() {
        return blockResistance;
    }

    public void set_blockResistance(float blockResistance) {
        this.blockResistance = blockResistance;
    }

    public boolean get_enableStats() {
        return enableStats;
    }

    public void set_enableStats(boolean enableStats) {
        this.enableStats = enableStats;
    }

    public boolean get_needsRandomTick() {
        return needsRandomTick;
    }

    public void set_needsRandomTick(boolean needsRandomTick) {
        this.needsRandomTick = needsRandomTick;
    }

    public boolean get_isBlockContainer() {
        return isBlockContainer;
    }

    public void set_isBlockContainer(boolean isBlockContainer) {
        this.isBlockContainer = isBlockContainer;
    }

    public SoundType get_blockSoundType() {
        return blockSoundType;
    }

    public void set_blockSoundType(SoundType blockSoundType) {
        this.blockSoundType = blockSoundType;
    }

    public Material get_blockMaterial() {
        return blockMaterial;
    }

    public MapColor get_blockMapColor() {
        return blockMapColor;
    }

    public BlockStateContainer get_blockState() {
        return blockState;
    }

    public ThreadLocal get_harvesters() {
        return harvesters;
    }

    public void set_harvesters(ThreadLocal harvesters) {
        this.harvesters = harvesters;
    }

    public Random get_RANDOM() {
        return RANDOM;
    }

    public void set_RANDOM(Random RANDOM) {
        this.RANDOM = RANDOM;
    }

    public ThreadLocal get_captureDrops() {
        return captureDrops;
    }

    public void set_captureDrops(ThreadLocal captureDrops) {
        this.captureDrops = captureDrops;
    }

    public ThreadLocal get_capturedDrops() {
        return capturedDrops;
    }

    public void set_capturedDrops(ThreadLocal capturedDrops) {
        this.capturedDrops = capturedDrops;
    }

    public List call_captureDrops(boolean arg1) {
        return captureDrops(arg1);
    }

    public Block call_setSoundType(SoundType arg1) {
        return setSoundType(arg1);
    }

    public RayTraceResult call_rayTrace(BlockPos arg1, Vec3d arg2, Vec3d arg3, AxisAlignedBB arg4) {
        return rayTrace(arg1, arg2, arg3, arg4);
    }

    public BlockStateContainer call_createBlockState() {
        return createBlockState();
    }

    public Block call_disableStats() {
        return disableStats();
    }

    public boolean call_canSilkHarvest() {
        return canSilkHarvest();
    }

    public void call_setDefaultState(IBlockState arg1) {
        setDefaultState(arg1);
    }

    public ItemStack call_createStackedBlock(IBlockState arg1) {
        return createStackedBlock(arg1);
    }

    public void call_addCollisionBoxToList(BlockPos arg1, AxisAlignedBB arg2, List arg3, AxisAlignedBB arg4) {
        addCollisionBoxToList(arg1, arg2, arg3, arg4);
    }

    @Override
    public boolean onBlockActivated(World arg1, BlockPos arg2, IBlockState arg3, EntityPlayer arg4, EnumHand arg5, ItemStack arg6, EnumFacing arg7, float arg8, float arg9, float arg10) {
        try {
            return (boolean) ReflectUtil.invoke("onBlockActivated", reloader.getInstance(this), arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            return false;
        }
    }

}
