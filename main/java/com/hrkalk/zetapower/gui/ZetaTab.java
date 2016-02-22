package com.hrkalk.zetapower.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.items.ModItems;

public class ZetaTab extends CreativeTabs {

    public static final ZetaTab instance = new ZetaTab(Main.MODID);

    public ZetaTab(String label) {
        super(label);
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.zetaIngot;
    }

}
