package com.hrkalk.zetapower.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BasicItem extends Item {
    public BasicItem(String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
}
