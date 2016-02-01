package com.hrkalk.zetapower.items;

import net.minecraft.item.ItemHoe;

import com.hrkalk.zetapower.ZetaTab;

public class ItemModHoe extends ItemHoe {

    public ItemModHoe(String unlocalizedName, ToolMaterial material) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

}
