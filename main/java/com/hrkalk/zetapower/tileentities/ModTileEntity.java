package com.hrkalk.zetapower.tileentities;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;

public class ModTileEntity extends TileEntity implements ITickable {

    @Override
    public void tick() {
        System.out.println("Hello, I'm a TileEntity!");
    }

}