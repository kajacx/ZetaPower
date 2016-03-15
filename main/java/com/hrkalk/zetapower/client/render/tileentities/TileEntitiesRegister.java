package com.hrkalk.zetapower.client.render.tileentities;

import com.hrkalk.zetapower.client.render.blocks.BlockRenderRegister;
import com.hrkalk.zetapower.tileentities.TileEntities;
import com.hrkalk.zetapower.tileentities.ZetaChest;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TileEntitiesRegister {

    public static void registerTileEntitiesRenderer() {
        BlockRenderRegister.reg(TileEntities.zetaChestBlock);

        ClientRegistry.bindTileEntitySpecialRenderer(ZetaChest.class, new ZetaTileEntityRenderer());
    }
}
