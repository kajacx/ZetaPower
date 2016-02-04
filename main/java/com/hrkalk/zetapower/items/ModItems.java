package com.hrkalk.zetapower.items;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
    public static ToolMaterial zetaMaterial = EnumHelper.addToolMaterial("zetaMaterial", 2, 1000, 7.0F, 3.0F, 20);
    public static ArmorMaterial zetaArmorMaterial = EnumHelper.addArmorMaterial("zetaArmorMaterial", "zetapower:zeta_armor", 25, new int[] { 3, 8, 6, 3 }, 20);

    public static Item zetaIngot;
    public static Item metaItem;

    //tools
    public static Item zetaSword;
    public static Item zetaPickaxe;
    public static Item zetaShovel;
    public static Item zetaHoe;
    public static Item zetaAxe;
    public static Item zetaMultitool;

    //armor
    public static Item zetaHelmet;
    public static Item zetaChestplate;
    public static Item zetaLeggings;
    public static Item zetaBoots;

    //food
    public static Item customSnack;

    public static void preInit() {
        GameRegistry.registerItem(zetaIngot = new BasicItem("zeta_ingot"), "zeta_ingot");
        GameRegistry.registerItem(metaItem = new MetaItem("meta_item"), "meta_item");

        //tools
        GameRegistry.registerItem(zetaSword = new ItemModSword("zeta_sword", zetaMaterial), "zeta_sword");
        GameRegistry.registerItem(zetaPickaxe = new ItemModPickaxe("zeta_pickaxe", zetaMaterial), "zeta_pickaxe");
        GameRegistry.registerItem(zetaShovel = new ItemModShovel("zeta_shovel", zetaMaterial), "zeta_shovel");
        GameRegistry.registerItem(zetaHoe = new ItemModHoe("zeta_hoe", zetaMaterial), "zeta_hoe");
        GameRegistry.registerItem(zetaAxe = new ItemModAxe("zeta_axe", zetaMaterial), "zeta_axe");
        GameRegistry.registerItem(zetaMultitool = new ItemModMultitool("zeta_multitool", zetaMaterial), "zeta_multitool");

        //armor
        GameRegistry.registerItem(zetaHelmet = new ItemModArmor("zeta_helmet", zetaArmorMaterial, 1, 0), "zeta_helmet");
        GameRegistry.registerItem(zetaChestplate = new ItemModArmor("zeta_chestplate", zetaArmorMaterial, 1, 1), "zeta_chestplate");
        GameRegistry.registerItem(zetaLeggings = new ItemModArmor("zeta_leggings", zetaArmorMaterial, 2, 2), "zeta_leggings");
        GameRegistry.registerItem(zetaBoots = new ItemModArmor("zeta_boots", zetaArmorMaterial, 1, 3), "zeta_boots");

        //food

        GameRegistry.registerItem(customSnack = new ModItemFood("custom_snack", 2, 0.2f, false,
                new PotionEffect(Potion.moveSpeed.id, 1200, 1),
                new PotionEffect(Potion.jump.id, 600, 0),
                new PotionEffect(Potion.regeneration.id, 200, 1))
                .setAlwaysEdible(), "custom_snack");
    }
}
