package com.hrkalk.zetapower.items;

import com.hrkalk.zetapower.dimension.MallocDimension;
import com.hrkalk.zetapower.dimension.SimpleTeleporter;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.entities.RideableShip;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ItemModSpawnShip_Reload {

    public ItemModSpawnShip thiz;

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        L.d("on item use--");
        if (worldIn.isRemote) {
            return EnumActionResult.PASS;
        }

        if (playerIn.isSneaking()) {
            RideableShip ship = new RideableShip(worldIn, playerIn.posX, playerIn.posY + 2, playerIn.posZ, null, null);
            worldIn.spawnEntityInWorld(ship);
        } else {

            MallocDimension dim = ZetaDimensionHandler.mallocDimension;
            DimensionType type = dim.type;
            int dimId = type.getId();
            L.d("dim id: " + dimId);

            //WorldServer server = (WorldServer) worldIn;
            EntityPlayerMP mp = (EntityPlayerMP) playerIn;
            if (playerIn.dimension != dimId) {
                mp.mcServer.getPlayerList().transferPlayerToDimension(mp, dimId, new SimpleTeleporter(DimensionManager.getWorld(dimId)));
            } else {
                mp.mcServer.getPlayerList().transferPlayerToDimension(mp, 0, new SimpleTeleporter(DimensionManager.getWorld(0)));
            }
        }

        return EnumActionResult.SUCCESS;
    }

}
