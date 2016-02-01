package com.hrkalk.zetapower.items;

import net.minecraft.item.ItemAxe;

import com.hrkalk.zetapower.ZetaTab;

public class ItemModAxe extends ItemAxe {

    public ItemModAxe(String unlocalizedName, ToolMaterial material) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

}
