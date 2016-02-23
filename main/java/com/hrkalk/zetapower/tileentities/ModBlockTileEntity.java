package com.hrkalk.zetapower.tileentities;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.gui.ModGuiHandler;
import com.hrkalk.zetapower.gui.ZetaTab;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ModBlockTileEntity extends BlockContainer {

    protected ModBlockTileEntity(String unlocalizedName) {
        super(Material.iron);
        setUnlocalizedName(unlocalizedName);
        setHardness(2.0f);
        setResistance(6.0f);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(ZetaTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ModTileEntity();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        ModTileEntity te = (ModTileEntity) world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, te);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        //super does nothing
        if (stack.hasDisplayName()) {
            ((ModTileEntity) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(Main.instance, ModGuiHandler.MOD_TILE_ENTITY_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
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