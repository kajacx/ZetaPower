package com.hrkalk.zetapower;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.hrkalk.zetapower.blocks.ModBlocks;
import com.hrkalk.zetapower.crafting.ModCrafting;
import com.hrkalk.zetapower.items.ModItems;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		ModItems.createItems();
		ModBlocks.createBlocks();
	}

	public void init(FMLInitializationEvent e) {
		ModCrafting.initCrafting();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}
}
