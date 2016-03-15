package com.hrkalk.zetapower.tileentities;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntities {
    public static Block zetaChestBlock;

    public static void preInit() {
        GameRegistry.registerBlock(zetaChestBlock = new ZetaChestBlock("zeta_chest_block"), "zeta_chest_block");
    }

    public static void init() {
        GameRegistry.registerTileEntity(ZetaChest.class, "zeta_chest");
    }

}