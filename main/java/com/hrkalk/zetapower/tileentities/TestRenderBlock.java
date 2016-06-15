package com.hrkalk.zetapower.tileentities;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TestRenderBlock extends BlockContainer {

    protected TestRenderBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TestRender();
    }

}
