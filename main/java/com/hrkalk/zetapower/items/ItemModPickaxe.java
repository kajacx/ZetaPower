package com.hrkalk.zetapower.items;

import net.minecraft.item.ItemPickaxe;

import com.hrkalk.zetapower.ZetaTab;

public class ItemModPickaxe extends ItemPickaxe {

    public ItemModPickaxe(String unlocalizedName, ToolMaterial material) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

}
