package com.hrkalk.zetapower.items;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.hrkalk.zetapower.utils.L;

import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
    public static ToolMaterial zetaMaterial = EnumHelper.addToolMaterial("zetaMaterial", 2, 1000, 7.0F, 3.0F, 20);
    public static ArmorMaterial zetaArmorMaterial = EnumHelper.addArmorMaterial("zetaArmorMaterial", "zetapower:zeta_armor", 25, new int[] { 3, 8, 6, 3 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);

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

    public static void addValue(Class<?> clazz, String arrName, float value) {
        try {
            Field f = clazz.getDeclaredField(arrName);
            f.setAccessible(true);
            float[] data = (float[]) f.get(null);
            float[] newData = new float[data.length + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[data.length] = value;

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            f.set(null, newData);
        } catch (Exception ex) {
            L.d("Failed to add value to array via reflection");
            //ex.printStackTrace(System.out);
            throw new RuntimeException(ex);
        }
    }

    public static void preInit() {

        // -- WARNING: UGLY HACK TO MAKE THINGS WORK. FORGE SHOULD FIX THIS, CUSTOM TOOL MATERIALS ARE BROKEN --
        //addValue(ItemAxe.class, "ATTACK_DAMAGES", 7f);
        //addValue(ItemAxe.class, "ATTACK_SPEEDS", -3f);
        //TODO: fix this madness

        GameRegistry.register((zetaIngot = new BasicItem("zeta_ingot")).setRegistryName("zeta_ingot"));
        GameRegistry.register((metaItem = new MetaItem("meta_item")).setRegistryName("meta_item"));

        //tools
        GameRegistry.register((zetaSword = new ItemModSword("zeta_sword", zetaMaterial)).setRegistryName("zeta_sword"));
        GameRegistry.register((zetaPickaxe = new ItemModPickaxe("zeta_pickaxe", zetaMaterial)).setRegistryName("zeta_pickaxe"));
        GameRegistry.register((zetaShovel = new ItemModShovel("zeta_shovel", zetaMaterial)).setRegistryName("zeta_shovel"));
        GameRegistry.register((zetaHoe = new ItemModHoe("zeta_hoe", zetaMaterial)).setRegistryName("zeta_hoe"));
        GameRegistry.register((zetaAxe = new ItemModAxe("zeta_axe", zetaMaterial, 7, -3)).setRegistryName("zeta_axe"));
        GameRegistry.register((zetaMultitool = new ItemModMultitool("zeta_multitool", zetaMaterial)).setRegistryName("zeta_multitool"));

        //armor
        GameRegistry.register((zetaHelmet = new ItemModArmor("zeta_helmet", zetaArmorMaterial, 1, EntityEquipmentSlot.HEAD)).setRegistryName("zeta_helmet"));
        GameRegistry.register((zetaChestplate = new ItemModArmor("zeta_chestplate", zetaArmorMaterial, 1, EntityEquipmentSlot.CHEST)).setRegistryName("zeta_chestplate"));
        GameRegistry.register((zetaLeggings = new ItemModArmor("zeta_leggings", zetaArmorMaterial, 2, EntityEquipmentSlot.LEGS)).setRegistryName("zeta_leggings"));
        GameRegistry.register((zetaBoots = new ItemModArmor("zeta_boots", zetaArmorMaterial, 1, EntityEquipmentSlot.FEET)).setRegistryName("zeta_boots"));

        //food

        GameRegistry.register((customSnack = new ModItemFood("custom_snack", 2, 0.2f, false,
                new PotionEffect(MobEffects.SPEED, 1200, 1),
                new PotionEffect(MobEffects.JUMP_BOOST, 600, 0),
                new PotionEffect(MobEffects.REGENERATION, 200, 1))
                        .setAlwaysEdible()).setRegistryName("custom_snack"));
    }
}
