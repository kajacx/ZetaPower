package com.hrkalk.zetapower.entities.vessel;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;
import com.hrkalk.zetapower.vessel.BlockCluster;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class VesselEntity extends Entity implements IEntityAdditionalSpawnData {

    public BlockCluster cluster;

    public Vector3f prevLookForw = new Vector3f();
    public Vector3f prevLookUp = new Vector3f();

    private DynamicReloader reloader = new DynamicReloader(VesselEntity.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.entities.vessel.VesselEntity.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(200));

        reloader.addToBlacklist("net.minecraft.nbt.NBTTagCompound");
        reloader.addToBlacklist("net.minecraft.network.datasync.EntityDataManager");
        reloader.addToBlacklist("net.minecraft.entity.Entity");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("net.minecraft.util.EnumFacing");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("net.minecraft.util.math.BlockPos");
        reloader.addToBlacklist("java.util.Random");
        reloader.addToBlacklist("net.minecraft.network.datasync.DataParameter");
        reloader.addToBlacklist("java.util.UUID");
        reloader.addToBlacklist("float[]");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("net.minecraft.block.state.IBlockState");
        reloader.addToBlacklist("net.minecraft.nbt.NBTTagList");
        reloader.addToBlacklist("net.minecraft.block.Block");
        reloader.addToBlacklist("net.minecraft.util.math.Vec3d");
        reloader.addToBlacklist("double[]");
        reloader.addToBlacklist("net.minecraft.entity.EntityLivingBase");
        reloader.addToBlacklist("net.minecraft.util.text.event.HoverEvent");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("com.hrkalk.zetapower.entities.RideableShip");
        reloader.addToBlacklist("java.lang.String");
        reloader.addToBlacklist("com.hrkalk.zetapower.entities.vessel.VesselEntity");

        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.dimension");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.vessel");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.client.input");
    }

    public VesselEntity(World worldIn) {
        this(worldIn, 0, 0, 0, null);
    }

    /**
     * 
     * @param worldIn
     * @param x center x pos
     * @param y center y pos
     * @param z center z pos
     * @param cluster
     */
    public VesselEntity(World worldIn, double x, double y, double z, BlockCluster cluster) {
        super(worldIn);
        float size = .95f;
        this.setSize(size, size);
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.cluster = cluster;

        //L.d("Creating vessel entity, entity: " + this);
        //L.d("Creating vessel entity, cluster: " + cluster);
        //L.d("Creating vessel entity, rotator: " + (cluster == null ? "---" : cluster.getRotator().toString()));
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float w2 = this.width / 2f;
        float h2 = this.height / 2f;
        this.setEntityBoundingBox(new AxisAlignedBB(x - w2, y - h2, z - w2, x + w2, y + h2, z + w2));
    }

    @Override
    public void resetPositionToBB() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        this.posY = (axisalignedbb.minY + axisalignedbb.maxY) / 2.0D;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }

    public int get_rideCooldown() {
        return rideCooldown;
    }

    public void set_rideCooldown(int rideCooldown) {
        this.rideCooldown = rideCooldown;
    }

    public boolean get_isInWeb() {
        return isInWeb;
    }

    public void set_isInWeb(boolean isInWeb) {
        this.isInWeb = isInWeb;
    }

    public Random get_rand() {
        return rand;
    }

    public void set_rand(Random rand) {
        this.rand = rand;
    }

    public boolean get_inWater() {
        return inWater;
    }

    public void set_inWater(boolean inWater) {
        this.inWater = inWater;
    }

    public boolean get_firstUpdate() {
        return firstUpdate;
    }

    public void set_firstUpdate(boolean firstUpdate) {
        this.firstUpdate = firstUpdate;
    }

    public boolean get_isImmuneToFire() {
        return isImmuneToFire;
    }

    public void set_isImmuneToFire(boolean isImmuneToFire) {
        this.isImmuneToFire = isImmuneToFire;
    }

    public EntityDataManager get_dataManager() {
        return dataManager;
    }

    public void set_dataManager(EntityDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DataParameter get_FLAGS() {
        return FLAGS;
    }

    public boolean get_inPortal() {
        return inPortal;
    }

    public void set_inPortal(boolean inPortal) {
        this.inPortal = inPortal;
    }

    public int get_portalCounter() {
        return portalCounter;
    }

    public void set_portalCounter(int portalCounter) {
        this.portalCounter = portalCounter;
    }

    public BlockPos get_lastPortalPos() {
        return lastPortalPos;
    }

    public void set_lastPortalPos(BlockPos lastPortalPos) {
        this.lastPortalPos = lastPortalPos;
    }

    public Vec3d get_lastPortalVec() {
        return lastPortalVec;
    }

    public void set_lastPortalVec(Vec3d lastPortalVec) {
        this.lastPortalVec = lastPortalVec;
    }

    public EnumFacing get_teleportDirection() {
        return teleportDirection;
    }

    public void set_teleportDirection(EnumFacing teleportDirection) {
        this.teleportDirection = teleportDirection;
    }

    public UUID get_entityUniqueID() {
        return entityUniqueID;
    }

    public void set_entityUniqueID(UUID entityUniqueID) {
        this.entityUniqueID = entityUniqueID;
    }

    public String get_cachedUniqueIdString() {
        return cachedUniqueIdString;
    }

    public void set_cachedUniqueIdString(String cachedUniqueIdString) {
        this.cachedUniqueIdString = cachedUniqueIdString;
    }

    public boolean get_glowing() {
        return glowing;
    }

    public void set_glowing(boolean glowing) {
        this.glowing = glowing;
    }

    public void call_setSize(float arg1, float arg2) {
        setSize(arg1, arg2);
    }

    public boolean call_getFlag(int arg1) {
        return getFlag(arg1);
    }

    public void call_setFlag(int arg1, boolean arg2) {
        setFlag(arg1, arg2);
    }

    public void call_readEntityFromNBT(NBTTagCompound arg1) {
        readEntityFromNBT(arg1);
    }

    public void call_writeEntityToNBT(NBTTagCompound arg1) {
        writeEntityToNBT(arg1);
    }

    public void call_setBeenAttacked() {
        setBeenAttacked();
    }

    public boolean call_canFitPassenger(Entity arg1) {
        return canFitPassenger(arg1);
    }

    public void call_entityInit() {
        entityInit();
    }

    public void call_kill() {
        kill();
    }

    public void call_setRotation(float arg1, float arg2) {
        setRotation(arg1, arg2);
    }

    public SoundEvent call_getSplashSound() {
        return getSplashSound();
    }

    public void call_updateFallState(double arg1, boolean arg2, IBlockState arg3, BlockPos arg4) {
        updateFallState(arg1, arg2, arg3, arg4);
    }

    public void call_resetHeight() {
        resetHeight();
    }

    public SoundEvent call_getSwimSound() {
        return getSwimSound();
    }

    public void call_dealFireDamage(int arg1) {
        dealFireDamage(arg1);
    }

    public void call_playStepSound(BlockPos arg1, Block arg2) {
        playStepSound(arg1, arg2);
    }

    public Vec3d call_getVectorForRotation(float arg1, float arg2) {
        return getVectorForRotation(arg1, arg2);
    }

    public boolean call_canTriggerWalking() {
        return canTriggerWalking();
    }

    public boolean call_shouldSetPosAfterLoading() {
        return shouldSetPosAfterLoading();
    }

    public void call_doBlockCollisions() {
        doBlockCollisions();
    }

    public void call_createRunningParticles() {
        createRunningParticles();
    }

    public void call_setOnFireFromLava() {
        setOnFireFromLava();
    }

    public void call_applyEnchantments(EntityLivingBase arg1, Entity arg2) {
        applyEnchantments(arg1, arg2);
    }

    public void call_preparePlayerToSpawn() {
        preparePlayerToSpawn();
    }

    public void call_decrementTimeUntilPortal() {
        decrementTimeUntilPortal();
    }

    public HoverEvent call_getHoverEvent() {
        return getHoverEvent();
    }

    public NBTTagList call_newDoubleNBTList(double[] arg1) {
        return newDoubleNBTList(arg1);
    }

    public boolean call_canBeRidden(Entity arg1) {
        return canBeRidden(arg1);
    }

    public String call_getEntityString() {
        return getEntityString();
    }

    public void call_removePassenger(Entity arg1) {
        removePassenger(arg1);
    }

    public boolean call_pushOutOfBlocks(double arg1, double arg2, double arg3) {
        return pushOutOfBlocks(arg1, arg2, arg3);
    }

    public NBTTagList call_newFloatNBTList(float[] arg1) {
        return newFloatNBTList(arg1);
    }

    public void call_addPassenger(Entity arg1) {
        addPassenger(arg1);
    }

    @Override
    public void onUpdate() {
        try {
            ReflectUtil.invoke("onUpdate", reloader.getInstance(this));
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound arg1) {
        try {
            ReflectUtil.invoke("readEntityFromNBT", reloader.getInstance(this), arg1);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound arg1) {
        try {
            ReflectUtil.invoke("writeEntityToNBT", reloader.getInstance(this), arg1);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        try {
            ReflectUtil.invoke("writeSpawnData", reloader.getInstance(this), buffer);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        try {
            ReflectUtil.invoke("readSpawnData", reloader.getInstance(this), additionalData);
        } catch (Throwable t) {
            L.e("Exception while executing reloadable code.", t);
            L.e(t.getCause());
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

    @Override
    protected void entityInit() {
        // TODO Auto-generated method stub

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
            try {
                return (boolean) ReflectUtil.invoke("processInitialInteract", reloader.getInstance(this), player, stack, hand);
            } catch (Throwable t) {
                System.out.println("Exception while executing reloadable code.");
                t.printStackTrace(System.out);
                return false;
            }
        } else {
            return true;
        }
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

}
