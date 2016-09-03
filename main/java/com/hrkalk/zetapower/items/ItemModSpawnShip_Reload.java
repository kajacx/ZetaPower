package com.hrkalk.zetapower.items;

import com.hrkalk.zetapower.dimension.MallocDimension;
import com.hrkalk.zetapower.dimension.SimpleTeleporter;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
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
import net.minecraft.world.WorldServer;

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

        // pos = pos.offset(facing);

        //RideableShip ship = new RideableShip(worldIn, playerIn.posX, playerIn.posY + 2, playerIn.posZ);
        //worldIn.spawnEntityInWorld(ship);

        /*if (playerIn instanceof EntityPlayerMP) {
            WorldServer worldserver = (WorldServer) worldIn;
            EntityPlayerMP var4 = (EntityPlayerMP) playerIn;
        
            var4.tele
            
            if (playerIn.getRidingEntity() == null && !playerIn.isBeingRidden() && var4.dimension != ZetaDimensionHandler.mallocDimension.type.getId()) {
                var4.mcServer.getCommandManager().transferPlayerToDimension(var4, ZetaDimensionHandler.mallocDimension.type.getId());//, new TutorialTeleporter(worldserver));
            }
            if (par3EntityPlayer.ridingEntity == null && par3EntityPlayer.riddenByEntity == null && par3EntityPlayer instanceof EntityPlayer && var4.dimension == Registration.dimID) {
                var4.mcServer.getCommandManager().transferPlayerToDimension(var4, 0, new TutorialTeleporter(worldserver));
            }
        }
        return par1ItemStack;*/
        MallocDimension dim = ZetaDimensionHandler.mallocDimension;
        DimensionType type = dim.type;
        int dimId = type.getId();
        L.d("dim id: " + dimId);



        WorldServer server = (WorldServer) worldIn;
        EntityPlayerMP mp = (EntityPlayerMP) playerIn;
        if (playerIn.dimension != dimId) {
            mp.mcServer.getPlayerList().transferPlayerToDimension(mp, dimId, new SimpleTeleporter(server));
        } else {
            mp.mcServer.getPlayerList().transferPlayerToDimension(mp, 0, new SimpleTeleporter(server));
        }

        return EnumActionResult.SUCCESS;
    }

}
