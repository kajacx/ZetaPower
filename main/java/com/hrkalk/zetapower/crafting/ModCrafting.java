package com.hrkalk.zetapower.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hrkalk.zetapower.blocks.ModBlocks;
import com.hrkalk.zetapower.items.ModItems;

public final class ModCrafting {
    public static void initCrafting(FMLInitializationEvent e) {
        GameRegistry.addRecipe(new ItemStack(ModBlocks.zetaOre, 2), "##", "##", '#', ModItems.zetaIngot);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.zetaOre), Blocks.sand, ModItems.zetaIngot);
        GameRegistry.addSmelting(ModBlocks.zetaOre, new ItemStack(ModItems.zetaIngot), .8f);

        //tools
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaSword), "#", "#", "$", '#', ModItems.zetaIngot, '$', Items.stick);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaPickaxe), "###", " $ ", " $ ", '#', ModItems.zetaIngot, '$', Items.stick);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaShovel), "#", "$", "$", '#', ModItems.zetaIngot, '$', Items.stick);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaHoe), "##", " $", " $", '#', ModItems.zetaIngot, '$', Items.stick);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaAxe), "##", "#$", " $", '#', ModItems.zetaIngot, '$', Items.stick);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaMultitool), "#$#", "#$#", " $ ", '#', ModItems.zetaIngot, '$', Items.stick);
    }
}
