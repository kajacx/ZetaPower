package com.hrkalk.zetapower.proxy;

import com.hrkalk.zetapower.client.input.InputHandler;
import com.hrkalk.zetapower.client.render.blocks.BlockRenderRegister;
import com.hrkalk.zetapower.client.render.camera.TestCamera;
import com.hrkalk.zetapower.client.render.entities.EntitiesRegister;
import com.hrkalk.zetapower.client.render.items.ItemRenderRegister;
import com.hrkalk.zetapower.client.render.tileentities.TileEntitiesRegister;

import net.minecraft.client.renderer.ccc.CustomCameraControl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        System.out.println("Client preinit");
        BlockRenderRegister.preInit();
        ItemRenderRegister.preInit();
        MinecraftForge.EVENT_BUS.register(new InputHandler());
        InputHandler.preInit();
        EntitiesRegister.preInit();

        //TEST: add a test camera
        CustomCameraControl.cameras.add(new TestCamera());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        System.out.println("Client init");
        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();
        TileEntitiesRegister.registerTileEntitiesRenderer();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        System.out.println("Client postinit");
    }

}
