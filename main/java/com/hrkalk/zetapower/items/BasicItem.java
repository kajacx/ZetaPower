package com.hrkalk.zetapower.items;

import com.hrkalk.zetapower.gui.ZetaTab;

import net.minecraft.item.Item;

public class BasicItem extends Item {
    public BasicItem(String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(ZetaTab.instance);
    }
}
