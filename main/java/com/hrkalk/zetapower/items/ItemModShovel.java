package com.hrkalk.zetapower.items;

import com.hrkalk.zetapower.gui.ZetaTab;

import net.minecraft.item.ItemSpade;

public class ItemModShovel extends ItemSpade {

    public ItemModShovel(String unlocalizedName, ToolMaterial material) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ZetaTab.instance);
    }

}
