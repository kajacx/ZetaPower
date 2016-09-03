package com.hrkalk.zetapower.proxy;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.blocks.ModBlocks;
import com.hrkalk.zetapower.blocks.ZetaWorldGen;
import com.hrkalk.zetapower.crafting.ModCrafting;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.entities.ModEntities;
import com.hrkalk.zetapower.event.EventHandlerCommon;
import com.hrkalk.zetapower.event.EventHandlerTET;
import com.hrkalk.zetapower.gui.ModGuiHandler;
import com.hrkalk.zetapower.items.ModItems;
import com.hrkalk.zetapower.tileentities.TileEntities;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        System.out.println("Common preinit");
        ModItems.preInit();
        ModBlocks.preInit();
        TileEntities.preInit();
    }

    public void init(FMLInitializationEvent e) {
        System.out.println("Common init");
        ModCrafting.initCrafting(e);
        GameRegistry.registerWorldGenerator(new ZetaWorldGen(), 0);
        TileEntities.init();
        ModEntities.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new ModGuiHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandlerCommon());
        MinecraftForge.EVENT_BUS.register(new EventHandlerTET());
        ZetaDimensionHandler.initDimensions();
    }

    public void postInit(FMLPostInitializationEvent e) {
        System.out.println("Common postinit");
    }
}
