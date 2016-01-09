package com.hrkalk.zetapower.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
    public static Item zetaIngot;
    public static Item metaItem;

    public static void init() {
        GameRegistry.registerItem(zetaIngot = new BasicItem("zeta_ingot"), "zeta_ingot");

        GameRegistry.registerItem(metaItem = new MetaItem("meta_item"), "meta_item");
    }
}
