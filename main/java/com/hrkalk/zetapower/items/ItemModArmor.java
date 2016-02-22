package com.hrkalk.zetapower.items;

import com.hrkalk.zetapower.gui.ZetaTab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemModArmor extends ItemArmor {

    public ItemModArmor(String unlocalizedName, ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (itemStack.getItem() == ModItems.zetaHelmet) {
            effectPlayer(player, Potion.waterBreathing, 0);
        }
        if (itemStack.getItem() == ModItems.zetaChestplate) {
            effectPlayer(player, Potion.digSpeed, 0);
        }
        if (itemStack.getItem() == ModItems.zetaLeggings) {
            effectPlayer(player, Potion.moveSpeed, 0);
        }
        if (itemStack.getItem() == ModItems.zetaBoots) {
            effectPlayer(player, Potion.jump, 1);
        }
        if (player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() == ModItems.zetaHelmet
                && player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() == ModItems.zetaChestplate
                && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() == ModItems.zetaLeggings
                && player.inventory.armorItemInSlot(0) != null && player.inventory.armorItemInSlot(0).getItem() == ModItems.zetaBoots) {
            this.effectPlayer(player, Potion.regeneration, 1);
        }
    }

    private void effectPlayer(EntityPlayer player, Potion potion, int amplifier) {
        //Always effect for 8 seconds, then refresh
        if (player.getActivePotionEffect(potion) == null || player.getActivePotionEffect(potion).getDuration() <= 1)
            player.addPotionEffect(new PotionEffect(potion.id, 159, amplifier, true, true));
    }
}