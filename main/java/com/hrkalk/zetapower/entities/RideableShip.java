package com.hrkalk.zetapower.entities;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.NBTReader;
import com.hrkalk.zetapower.utils.loader.FilteredClassLoader;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class RideableShip extends Entity {// EntityAnimal {

    private Object instance = null;
    private long lastLoaded = 0;
    private static File file = new File("../bin/com/hrkalk/zetapower/entities/RideableShipReload.class");

    public RideableShip(World worldIn) {
        this(worldIn, 0, 0, 0);
    }

    public RideableShip(World worldIn, double x, double y, double z) {
        super(worldIn);
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setSize(0.9F, 0.9F);
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        //TODO: multiple passangers
        return this.getPassengers().size() < 1;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
        L.d("interact");
        if (!super.processInitialInteract(player, stack, hand)) {
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
    public void onUpdate() {
        super.onUpdate();
        if (this.isBeingRidden()) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) this.getControllingPassenger();

            try {
                if (file.lastModified() > lastLoaded || true) {

                    lastLoaded = file.lastModified();

                    Class<?> clazz = loadClass();

                    Object i = ReflectUtil.newInstance(clazz);

                    instance = i;

                    ReflectUtil.invoke("moveEntity", instance, this, entityLivingBase);
                }

            } catch (Throwable e) {
                L.e("Error in rendering via dynamic classload");
                e.printStackTrace(System.out);
            } //*/
        }
    }


    private List<String> whitelist = Arrays.asList("RideableShipReload", "zetapower.utils");

    private Class<?> loadClass() {
        FilteredClassLoader classLoader = new FilteredClassLoader((className) -> whitelist.stream().anyMatch(whitelisted -> {
            //L.d("Whitelisted: " + whitelisted);
            //L.d("ClassName: " + className);
            //L.d("Result: " + className.contains(whitelisted));
            return className.contains(whitelisted);
        }), "../bin");
        Class<?> contextClass = classLoader.load("com.hrkalk.zetapower.entities.RideableShipReload");
        return contextClass;
        //return new DynamicClassLoader("../bin").load("com.hrkalk.zetapower.entities.RideableShipReload");
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }

    /**
     * Returns the collision bounding box for this entity
     */
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected void entityInit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        compound.setDouble("posX", this.posX);
        compound.setDouble("posY", this.posY);
        compound.setDouble("posZ", this.posZ);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        this.posX = NBTReader.readDoubleOr(compound, "posX", 0);
        this.posY = NBTReader.readDoubleOr(compound, "posY", 0);
        this.posZ = NBTReader.readDoubleOr(compound, "posZ", 0);
    }

    public interface IRideableShipReload {
        public void init(double rotationXZ, double rotationY);

        public double getX();

        public double getY();

        public double getZ();
    }

}