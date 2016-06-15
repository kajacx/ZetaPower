package com.hrkalk.zetapower.blocks;

import com.hrkalk.zetapower.items.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks {

    public static Block zetaOre;
    public static Block propertyBlock;
    public static Block zetaOre2;
    public static Block zetaOre3;

    public static void preInit() {
        zetaOre = new BasicBlock("zeta_ore");
        zetaOre.setRegistryName("zeta_ore");
        GameRegistry.register(zetaOre);

        propertyBlock = new BlockProperties("block_properties", Material.WOOD, 1, 1);
        GameRegistry.register(propertyBlock.setRegistryName("block_properties"));

        GameRegistry.register(zetaOre2 = new ModBlockOre("zeta_ore_2", Material.ROCK, ModItems.zetaIngot, 2, 4).setRegistryName("zeta_ore_2"));
        GameRegistry.register(zetaOre3 = new ModBlockOre("zeta_ore_3", Material.ROCK, ModItems.zetaIngot).setRegistryName("zeta_ore_3"));
    }
}
