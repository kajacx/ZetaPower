package com.hrkalk.zetapower.items;

import com.hrkalk.zetapower.utils.L;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemModSpawnShip extends BasicItem {

    public ItemModSpawnShip(String unlocalizedName) {
        super(unlocalizedName);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }

        pos = pos.offset(facing);

        //Entity entity = spawnCreature(worldIn, getEntityIdFromItem(stack), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

        L.i("This really doesnt do anything yet");

        return EnumActionResult.SUCCESS;
    }
}
