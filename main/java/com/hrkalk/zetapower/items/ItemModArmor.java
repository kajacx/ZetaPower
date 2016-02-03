package com.hrkalk.zetapower.items;

import net.minecraft.item.ItemArmor;

import com.hrkalk.zetapower.ZetaTab;

public class ItemModArmor extends ItemArmor {

    public ItemModArmor(String unlocalizedName, ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }
}