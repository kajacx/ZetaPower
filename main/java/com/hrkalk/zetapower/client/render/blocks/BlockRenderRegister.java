package com.hrkalk.zetapower.client.render.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.blocks.ModBlocks;

public final class BlockRenderRegister {

    public static void preInit() {
        ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.propertyBlock), "zetapower:block_properties_black", "zetapower:block_properties_white");
    }

    public static void registerBlockRenderer() {
        reg(ModBlocks.tutorialBlock);
        reg(ModBlocks.propertyBlock, 0, "block_properties_white");
        reg(ModBlocks.propertyBlock, 1, "block_properties_black");
    }

    public static String modid = Main.MODID;

    public static void reg(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }

    public static void reg(Block block, int meta, String file) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(modid + ":" + file, "inventory"));
    }

}
