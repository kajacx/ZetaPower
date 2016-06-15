package com.hrkalk.zetapower.items;

import com.hrkalk.zetapower.gui.ZetaTab;

import net.minecraft.item.ItemAxe;

public class ItemModAxe extends ItemAxe {

    public ItemModAxe(String unlocalizedName, ToolMaterial material, float damage, float speed) {
        super(material, damage, speed);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

}
