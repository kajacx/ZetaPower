package com.hrkalk.zetapower.items;

import net.minecraft.item.ItemSword;

import com.hrkalk.zetapower.ZetaTab;

public class ItemModSword extends ItemSword {

    public ItemModSword(String unlocalizedName, ToolMaterial material) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

}
