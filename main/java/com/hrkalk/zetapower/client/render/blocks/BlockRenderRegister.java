package com.hrkalk.zetapower.client.render.blocks;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.blocks.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderRegister {

    public static void preInit() {
        /*
         * TODO: fix this
         * Item i = Item.getItemFromBlock(ModBlocks.propertyBlock);
        L.d("My item: " + i);
        ModelBakery.registerItemVariants(i,
                new ResourceLocation("zetapower", "block_properties_and"),
                new ResourceLocation("zetapower", "block_properties_or"));*/
    }

    public static void registerBlockRenderer() {
        reg(ModBlocks.zetaOre);
        reg(ModBlocks.propertyBlock, 0, "block_properties_and");
        reg(ModBlocks.propertyBlock, 1, "block_properties_or");
        reg(ModBlocks.zetaOre2);
        reg(ModBlocks.zetaOre3);
        reg(ModBlocks.testTeleporter);
    }

    public static void reg(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Main.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }

    public static void reg(Block block, int meta, String file) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Main.MODID + ":" + file, "inventory"));
    }
}
