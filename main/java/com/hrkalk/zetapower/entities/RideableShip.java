package com.hrkalk.zetapower.entities;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RideableShip extends EntityAnimal {

    public RideableShip(World worldIn) {
        super(worldIn);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (super.interact(player)) {
            return true;
        } else if (this.worldObj.isRemote || this.riddenByEntity != null && this.riddenByEntity != player) {
            return false;
        } else {
            player.mountEntity(this);
            return true;
        }
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        //This method shouldn't be called, since you can't breed airships
        return null;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        //You cannot breed a ship, dummy....
        return false;
    }
}
