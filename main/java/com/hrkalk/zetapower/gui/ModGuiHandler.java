package com.hrkalk.zetapower.gui;

import com.hrkalk.zetapower.tileentities.ModTileEntity;
import com.hrkalk.zetapower.utils.Log;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

    public static final int MOD_TILE_ENTITY_GUI = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == MOD_TILE_ENTITY_GUI) {
            TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
            if (entity instanceof ModTileEntity) {
                return new ContainerModTileEntity(player.inventory, (ModTileEntity) entity);
            } else {
                Log.error("Opening inventory of wrong tile entity");
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == MOD_TILE_ENTITY_GUI) {
            TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
            if (entity instanceof ModTileEntity) {
                return new GuiModTileEntity(player.inventory, (ModTileEntity) entity);
            } else {
                Log.error("Opening inventory of wrong tile entity");
            }
        }

        return null;
    }
}
