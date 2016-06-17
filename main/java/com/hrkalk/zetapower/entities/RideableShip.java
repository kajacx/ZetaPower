package com.hrkalk.zetapower.entities;

import static net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class RideableShip extends EntityAnimal {

    public RideableShip(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.9F);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (!super.processInteract(player, hand, stack)) {
            if (!this.worldObj.isRemote && !this.isBeingRidden()) {
                player.startRiding(this);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(MAX_HEALTH).setBaseValue(10.0D);
        getEntityAttribute(MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    public boolean canBeSteered() {
        Entity entity = this.getControllingPassenger();

        return entity instanceof EntityPlayer;
    }

    @Override
    public boolean canPassengerSteer() {
        return true;
    }

    /**
     * Moves the entity based on the specified heading.
     */
    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        super.moveEntityWithHeading(strafe, forward);
    }

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Override
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    //TODO: make co-pilots happen
    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 1;
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

    @Override
    public boolean isOnLadder() {
        // this better doesn't happen...
        return false;
    }
}
