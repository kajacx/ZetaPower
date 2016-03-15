package com.hrkalk.zetapower.tileentities;

import java.util.Random;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.gui.ModGuiHandler;
import com.hrkalk.zetapower.gui.ZetaTab;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ZetaChestBlock extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    protected ZetaChestBlock(String unlocalizedName) {
        super(Material.wood);
        setUnlocalizedName(unlocalizedName);
        setHardness(2.0f);
        setResistance(6.0f);
        setCreativeTab(ZetaTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ZetaChest();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        ZetaChest te = (ZetaChest) world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, te);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        Item item = super.getItemDropped(state, rand, fortune);

        return item;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        //set facing
        L.d("rotatioYaw " + placer.rotationYaw);
        L.d("rotatioHeadYaw " + placer.rotationYawHead);
        //EnumFacing facing = EnumFacing.getFacingFromVector(MathHelper.cos(placer.rotationYaw), 0, MathHelper.sin(placer.rotationYaw));
        EnumFacing facing = EnumFacing.fromAngle(placer.rotationYaw).getOpposite();

        //set display name
        if (stack.hasDisplayName()) {
            ((ZetaChest) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }

        //set facing
        ((ZetaChest) worldIn.getTileEntity(pos)).setEnumFacing(facing);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(Main.instance, ModGuiHandler.ZETA_CHEST_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1);
        if (world.getTileEntity(pos) instanceof ZetaChest) {
            ZetaChest te = (ZetaChest) world.getTileEntity(pos);
            if (te.hasCustomName()) {
                stack.setStackDisplayName(te.getCustomName());
                L.d("Setting stacks display name to: " + te.getCustomName());
            }
        }
        return stack;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }

}