package com.hrkalk.zetapower.tileentities;

import com.hrkalk.zetapower.ZetaTab;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
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
}