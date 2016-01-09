package com.hrkalk.zetapower.client.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.items.ModItems;

public final class ItemRenderRegister {

    public static void preInit() {
        ModelBakery.addVariantName(ModItems.metaItem, "zetapower:meta_item_a", "zetapower:meta_item_b");
    }

    public static void registerItemRenderer() {
        reg(ModItems.zetaIngot);
        reg(ModItems.metaItem, 0, "meta_item_a");
        reg(ModItems.metaItem, 1, "meta_item_b");
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