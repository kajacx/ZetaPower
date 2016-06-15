package com.hrkalk.zetapower.gui;

import com.hrkalk.zetapower.tileentities.ZetaChest;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

    public static final int ZETA_CHEST_GUI = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ZETA_CHEST_GUI) {
            TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
            if (entity instanceof ZetaChest) {
                return new ContainerZetaChest(player.inventory, (ZetaChest) entity);
            } else {
                L.e("Opening inventory of wrong tile entity");
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ZETA_CHEST_GUI) {
            TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
            if (entity instanceof ZetaChest) {
                return new GuiZetaChest(player.inventory, (ZetaChest) entity);
            } else {
                L.e("Opening inventory of wrong tile entity");
            }
        }

        return null;
    }
}
