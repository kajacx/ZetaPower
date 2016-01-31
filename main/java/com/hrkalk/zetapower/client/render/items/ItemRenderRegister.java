package com.hrkalk.zetapower.client.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.items.ModItems;

public final class ItemRenderRegister {

    public static void preInit() {
        ModelBakery.addVariantName(ModItems.metaItem, "zetapower:meta_item_white", "zetapower:meta_item_black");
    }

    public static void registerItemRenderer() {
        reg(ModItems.tutorialItem);
        reg(ModItems.metaItem, 0, "meta_item_white");
        reg(ModItems.metaItem, 1, "meta_item_black");
    }

    //==========================================================================

    public static String modid = Main.MODID;

    public static void reg(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

    public static void reg(Item item, int meta, String file) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(modid + ":" + file, "inventory"));
    }
}
