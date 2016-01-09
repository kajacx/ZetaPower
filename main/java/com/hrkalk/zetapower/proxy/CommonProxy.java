package com.hrkalk.zetapower.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.hrkalk.zetapower.blocks.ModBlocks;
import com.hrkalk.zetapower.crafting.ModCrafting;
import com.hrkalk.zetapower.items.ModItems;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        System.out.println("Common preinit");
        ModItems.init();
        ModBlocks.createBlocks();
    }

    public void init(FMLInitializationEvent e) {
        System.out.println("Common init");
        ModCrafting.initCrafting(e);
    }

    public void postInit(FMLPostInitializationEvent e) {
        System.out.println("Common postinit");
    }
}
