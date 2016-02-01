package com.hrkalk.zetapower.items;

import net.minecraft.item.Item;

import com.hrkalk.zetapower.ZetaTab;

public class BasicItem extends Item {
    public BasicItem(String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(ZetaTab.instance);
    }
}
