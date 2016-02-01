package com.hrkalk.zetapower.items;

import net.minecraft.item.ItemSpade;

import com.hrkalk.zetapower.ZetaTab;

public class ItemModShovel extends ItemSpade {

    public ItemModShovel(String unlocalizedName, ToolMaterial material) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

}
