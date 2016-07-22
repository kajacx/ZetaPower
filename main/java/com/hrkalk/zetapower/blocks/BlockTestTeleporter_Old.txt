package com.hrkalk.zetapower.blocks;

import com.hrkalk.zetapower.gui.ZetaTab;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTestTeleporter extends Block {

    protected BlockTestTeleporter(String unlocalizedName) {
        super(Material.PISTON);
        setUnlocalizedName(unlocalizedName);
        setHardness(2.0f);
        setResistance(6.0f);
        setCreativeTab(ZetaTab.instance);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        L.d("on activated");
        return true;
    }

}
