package com.hrkalk.zetapower.proxy;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.blocks.ModBlocks;
import com.hrkalk.zetapower.blocks.ZetaWorldGen;
import com.hrkalk.zetapower.crafting.ModCrafting;
import com.hrkalk.zetapower.gui.ModGuiHandler;
import com.hrkalk.zetapower.items.ModItems;
import com.hrkalk.zetapower.tileentities.ModTileEntities;

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
        ModTileEntities.preInit();
    }

    public void init(FMLInitializationEvent e) {
        System.out.println("Common init");
        ModCrafting.initCrafting(e);
        GameRegistry.registerWorldGenerator(new ZetaWorldGen(), 0);
        ModTileEntities.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new ModGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
        System.out.println("Common postinit");
    }
}
