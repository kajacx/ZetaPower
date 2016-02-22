package com.hrkalk.zetapower.items;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.hrkalk.zetapower.gui.ZetaTab;

public class ItemModMultitool extends ItemPickaxe {
    private static Set<Block> effectiveAgainst = Sets.newHashSet(new Block[] {
            Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel,
            Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland,
            Blocks.soul_sand, Blocks.mycelium });

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
    public boolean canHarvestBlock(Block block) {
        return effectiveAgainst.contains(block) || super.canHarvestBlock(block);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        return effectiveAgainst.contains(block) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, block);
    }
}