package com.hrkalk.zetapower.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.hrkalk.zetapower.client.render.blocks.BlockRenderRegister;
import com.hrkalk.zetapower.client.render.items.ItemRenderRegister;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        System.out.println("Client preinit");
        BlockRenderRegister.preInit();
        ItemRenderRegister.preInit();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        System.out.println("Client init");
        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        System.out.println("Client postinit");
    }

}
