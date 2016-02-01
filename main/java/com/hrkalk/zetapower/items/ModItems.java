package com.hrkalk.zetapower.items;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
    public static ToolMaterial zetaMaterial = EnumHelper.addToolMaterial("zetaMaterial", 2, 1000, 7.0F, 3.0F, 20);

    public static Item zetaIngot;
    public static Item metaItem;

    //tools
    public static Item zetaSword;
    public static Item zetaPickaxe;
    public static Item zetaShovel;
    public static Item zetaHoe;
    public static Item zetaAxe;
    public static Item zetaMultitool;

    public static void init() {
        GameRegistry.registerItem(zetaIngot = new BasicItem("zeta_ingot"), "zeta_ingot");
        GameRegistry.registerItem(metaItem = new MetaItem("meta_item"), "meta_item");

        //tools
        GameRegistry.registerItem(zetaSword = new ItemModSword("zeta_sword", zetaMaterial), "zeta_sword");
        GameRegistry.registerItem(zetaPickaxe = new ItemModPickaxe("zeta_pickaxe", zetaMaterial), "zeta_pickaxe");
        GameRegistry.registerItem(zetaShovel = new ItemModShovel("zeta_shovel", zetaMaterial), "zeta_shovel");
        GameRegistry.registerItem(zetaHoe = new ItemModHoe("zeta_hoe", zetaMaterial), "zeta_hoe");
        GameRegistry.registerItem(zetaAxe = new ItemModAxe("zeta_axe", zetaMaterial), "zeta_axe");
        GameRegistry.registerItem(zetaMultitool = new ItemModMultitool("zeta_multitool", zetaMaterial), "zeta_multitool");

    }
}
