package com.hrkalk.zetapower.client.render.tileentities;

import com.hrkalk.zetapower.client.render.blocks.BlockRenderRegister;
import com.hrkalk.zetapower.tileentities.ModTileEntities;
import com.hrkalk.zetapower.tileentities.ModTileEntity;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TileEntitiesRegister {

    public static void registerTileEntitiesRenderer() {
        BlockRenderRegister.reg(ModTileEntities.tileEntity);

        ClientRegistry.bindTileEntitySpecialRenderer(ModTileEntity.class, new ZetaTileEntityRenderer());
    }
}
