package com.hrkalk.zetapower.tileentities;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static Block tileEntity;

    public static void preInit() {
        GameRegistry.registerBlock(tileEntity = new ModBlockTileEntity("zeta_tile_entity"), "zeta_tile_entity");
    }

    public static void init() {
        GameRegistry.registerTileEntity(ModTileEntity.class, "zeta_tile_entity");
    }

}