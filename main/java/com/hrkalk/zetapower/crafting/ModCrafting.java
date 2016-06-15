package com.hrkalk.zetapower.crafting;

import com.hrkalk.zetapower.blocks.ModBlocks;
import com.hrkalk.zetapower.items.ModItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModCrafting {
    public static void initCrafting(FMLInitializationEvent e) {
        GameRegistry.addRecipe(new ItemStack(ModBlocks.zetaOre, 2), "##", "##", '#', ModItems.zetaIngot); //broken, zeta ore not visible
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.zetaOre), Blocks.SAND, ModItems.zetaIngot); //broken, zeta ore not visible
        GameRegistry.addSmelting(ModBlocks.zetaOre, new ItemStack(ModItems.zetaIngot), .8f); //broken, zeta ore not visible

        //tools
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaSword), "#", "#", "$", '#', ModItems.zetaIngot, '$', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaPickaxe), "###", " $ ", " $ ", '#', ModItems.zetaIngot, '$', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaShovel), "#", "$", "$", '#', ModItems.zetaIngot, '$', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaHoe), "##", " $", " $", '#', ModItems.zetaIngot, '$', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaAxe), "##", "#$", " $", '#', ModItems.zetaIngot, '$', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaMultitool), "#$#", "#$#", " $ ", '#', ModItems.zetaIngot, '$', Items.STICK);

        //armor
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaHelmet), "###", "# #", '#', ModItems.zetaIngot);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaChestplate), "# #", "###", "###", '#', ModItems.zetaIngot);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaLeggings), "###", "# #", "# #", '#', ModItems.zetaIngot);
        GameRegistry.addRecipe(new ItemStack(ModItems.zetaBoots), "# #", "# #", '#', ModItems.zetaIngot);

        //food
        GameRegistry.addRecipe(new ItemStack(ModItems.customSnack), "#$#", '#', ModItems.zetaIngot, '$', Items.BREAD);
    }
}
