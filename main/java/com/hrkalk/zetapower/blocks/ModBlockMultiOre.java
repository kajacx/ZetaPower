package com.hrkalk.zetapower.blocks;

import java.util.ArrayList;

import com.hrkalk.zetapower.gui.ZetaTab;
import com.hrkalk.zetapower.items.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ModBlockMultiOre extends Block {

    protected ModBlockMultiOre(String unlocalizedName, Material material) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(ZetaTab.instance);
        this.setSoundType(SoundType.STONE);
        this.setHardness(10.0f);
        this.setResistance(20.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState blockstate, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Items.COAL, RANDOM.nextInt(3) + 1));
        drops.add(new ItemStack(Items.IRON_INGOT, RANDOM.nextInt(2) + 1));
        drops.add(new ItemStack(Items.GOLD_INGOT, RANDOM.nextInt(2) + 1));
        drops.add(new ItemStack(Items.DYE, RANDOM.nextInt(3) + 2, 4));
        drops.add(new ItemStack(Items.REDSTONE, RANDOM.nextInt(2) + 2));
        drops.add(new ItemStack(ModItems.zetaIngot, RANDOM.nextInt(2) + 1));
        if (RANDOM.nextFloat() < 0.5F)
            drops.add(new ItemStack(Items.DIAMOND));
        return drops;
    }
}