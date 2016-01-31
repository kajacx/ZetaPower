package com.hrkalk.zetapower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks {

    public static Block zetaOre;
    public static Block propertyBlock;

    public static void createBlocks() {
        GameRegistry.registerBlock(zetaOre = new BasicBlock("zeta_ore"), "zeta_ore");

        GameRegistry.registerBlock(propertyBlock = new BlockProperties("block_properties", Material.wood, 1, 1),
                ItemBlockMeta.class, "block_properties");
    }
}
