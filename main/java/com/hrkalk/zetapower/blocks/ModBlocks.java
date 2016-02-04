package com.hrkalk.zetapower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hrkalk.zetapower.items.ModItems;

public final class ModBlocks {

    public static Block zetaOre;
    public static Block propertyBlock;
    public static Block zetaOre2;
    public static Block zetaOre3;

    public static void preInit() {
        GameRegistry.registerBlock(zetaOre = new BasicBlock("zeta_ore"), "zeta_ore");

        GameRegistry.registerBlock(propertyBlock = new BlockProperties("block_properties", Material.wood, 1, 1),
                ItemBlockMeta.class, "block_properties");

        GameRegistry.registerBlock(zetaOre2 = new ModBlockOre("zeta_ore_2", Material.rock, ModItems.zetaIngot, 2, 4),
                "zeta_ore_2");
        GameRegistry.registerBlock(zetaOre3 = new ModBlockMultiOre("zeta_ore_3", Material.rock), "zeta_ore_3");
    }
}
