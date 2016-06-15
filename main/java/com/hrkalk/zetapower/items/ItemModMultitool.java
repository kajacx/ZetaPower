package com.hrkalk.zetapower.items;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.hrkalk.zetapower.gui.ZetaTab;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemModMultitool extends ItemPickaxe {
    private static Set<Block> effectiveAgainst = Sets.newHashSet(new Block[] {
            Blocks.GRASS, Blocks.DIRT, Blocks.SAND, Blocks.GRAVEL,
            Blocks.SNOW_LAYER, Blocks.SNOW, Blocks.CLAY, Blocks.FARMLAND,
            Blocks.SOUL_SAND, Blocks.MYCELIUM });

    protected ItemModMultitool(String unlocalizedName, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "spade");
    }

    @Override
    public boolean canHarvestBlock(IBlockState block) {
        return effectiveAgainst.contains(block) || super.canHarvestBlock(block);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState block) {
        return effectiveAgainst.contains(block) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, block);
    }
}