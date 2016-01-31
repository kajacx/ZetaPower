package com.hrkalk.zetapower.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hrkalk.zetapower.blocks.ModBlocks;
import com.hrkalk.zetapower.items.ModItems;

public final class ModCrafting {

    public static void initCrafting() {
        GameRegistry.addRecipe(new ItemStack(ModBlocks.zetaOre), "##", "##", '#', ModItems.zetaIngot);
        //or: GameRegistry.addRecipe(new ItemStack(ModBlocks.tutorialBlock), new Object[]{"##","##", '#', ModItems.tutorialItem});

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.zetaIngot), Items.redstone, new ItemStack(Items.dye, 1, 4));
        //or: GameRegistry.addShapelessRecipe(new ItemStack(ModItems.tutorialItem), new Object[]{Items.redstone, new ItemStack(Items.dye, 1, 4)});

        GameRegistry.addSmelting(Items.diamond, new ItemStack(ModItems.zetaIngot), 1.0f);
    }
}
