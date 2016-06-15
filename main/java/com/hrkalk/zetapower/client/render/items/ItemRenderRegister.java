package com.hrkalk.zetapower.client.render.items;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.items.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class ItemRenderRegister {

    public static void preInit() {
        ModelBakery.registerItemVariants(ModItems.metaItem,
                new ResourceLocation("zetapower", "meta_item_a"),
                new ResourceLocation("zetapower", "meta_item_b"));
    }

    public static void registerItemRenderer() {
        reg(ModItems.zetaIngot);
        /*reg(ModItems.metaItem, 0, "meta_item_a");
        reg(ModItems.metaItem, 1, "meta_item_b");*/

        //tools
        reg(ModItems.zetaSword);
        reg(ModItems.zetaPickaxe);
        reg(ModItems.zetaShovel);
        reg(ModItems.zetaHoe);
        reg(ModItems.zetaAxe);
        reg(ModItems.zetaMultitool);

        //armor
        reg(ModItems.zetaHelmet);
        reg(ModItems.zetaChestplate);
        reg(ModItems.zetaLeggings);
        reg(ModItems.zetaBoots);

        //food
        reg(ModItems.customSnack);
    }

    public static void reg(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, 0, new ModelResourceLocation(Main.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

    public static void reg(Item item, int meta, String file) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, meta, new ModelResourceLocation(Main.MODID + ":" + file, "inventory"));
    }
}