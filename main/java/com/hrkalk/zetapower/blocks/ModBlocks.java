package com.hrkalk.zetapower.blocks;

import com.hrkalk.zetapower.items.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks {

    public static Block propertyBlock;
    public static Block zetaOre;
    public static Block zetaOre2;
    public static Block zetaOre3;
    public static Block testTeleporter;
    public static Block shipCoreTier1;

    public static void preInit() {

        propertyBlock = new BlockProperties("block_properties", Material.WOOD, 1, 1);
        GameRegistry.register(propertyBlock.setRegistryName("block_properties"));

        GameRegistry.register(zetaOre = new BasicBlock("zeta_ore").setRegistryName("zeta_ore"));
        GameRegistry.register(new ItemBlock(zetaOre).setRegistryName("zeta_ore"));

        GameRegistry.register(zetaOre2 = new ModBlockOre("zeta_ore_2", Material.ROCK, ModItems.zetaIngot, 2, 4).setRegistryName("zeta_ore_2"));
        GameRegistry.register(new ItemBlock(zetaOre2).setRegistryName("zeta_ore_2"));

        GameRegistry.register(zetaOre3 = new ModBlockOre("zeta_ore_3", Material.ROCK, ModItems.zetaIngot).setRegistryName("zeta_ore_3"));
        GameRegistry.register(new ItemBlock(zetaOre3).setRegistryName("zeta_ore_3"));

        GameRegistry.register(testTeleporter = new BlockTestTeleporter("test_teleporter").setRegistryName("test_teleporter"));
        GameRegistry.register(new ItemBlock(testTeleporter).setRegistryName("test_teleporter"));

        GameRegistry.register(shipCoreTier1 = new ModBlockShipCoreTier1("ship_core_tier_1").setRegistryName("ship_core_tier_1"));
        GameRegistry.register(new ItemBlock(shipCoreTier1).setRegistryName("ship_core_tier_1"));
    }
}
