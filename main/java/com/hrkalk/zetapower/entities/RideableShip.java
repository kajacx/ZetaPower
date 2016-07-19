package com.hrkalk.zetapower.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.hrkalk.zetapower.items.ModItems;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RideableShip extends EntityAnimal implements IInventoryChangedListener, IJumpingMount {
    private static final Predicate<Entity> IS_HORSE_BREEDING = new Predicate<Entity>() {
        @Override
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof EntityHorse && ((EntityHorse) p_apply_1_).isBreeding();
        }
    };
    private static final IAttribute JUMP_STRENGTH = (new RangedAttribute((IAttribute) null, "horse.jumpStrength", 0.7D,
            0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    private static final DataParameter<Byte> STATUS = EntityDataManager.<Byte>createKey(EntityHorse.class,
            DataSerializers.BYTE);
    private static final DataParameter<Integer> HORSE_TYPE = EntityDataManager.<Integer>createKey(EntityHorse.class,
            DataSerializers.VARINT);
    private static final DataParameter<Integer> HORSE_VARIANT = EntityDataManager.<Integer>createKey(EntityHorse.class,
            DataSerializers.VARINT);
    private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager
            .<Optional<UUID>>createKey(EntityHorse.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> HORSE_ARMOR = EntityDataManager.<Integer>createKey(EntityHorse.class,
            DataSerializers.VARINT);

    private int jumpRearingCounter;
    public int sprintCounter;
    protected boolean horseJumping;
    /**
     * "The higher this value, the more likely the horse is to be tamed next
     * time a player rides it."
     */
    protected int temper;
    protected float jumpPower;
    private boolean allowStandSliding;
    private boolean skeletonTrap;
    private int skeletonTrapTime = 0;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;
    /** Used to determine the sound that the horse should make when it steps */
    private int gallopTime;
    private String texturePrefix;
    private String[] horseTexturesArray = new String[3];
    private boolean hasTexture = false;

    public RideableShip(World worldIn) {
        super(worldIn);
        this.setSize(1.3964844F, 1.6F);
        this.isImmuneToFire = false;
        this.setChested(false);
        this.stepHeight = 1.0F;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWander(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(STATUS, Byte.valueOf((byte) 0));
        this.dataManager.register(HORSE_TYPE, Integer.valueOf(HorseType.HORSE.getOrdinal()));
        this.dataManager.register(HORSE_VARIANT, Integer.valueOf(0));
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.<UUID>absent());
        this.dataManager.register(HORSE_ARMOR, Integer.valueOf(HorseArmorType.NONE.getOrdinal()));
    }

    /*public void setType(HorseType armorType) {
        this.dataManager.set(HORSE_TYPE, Integer.valueOf(armorType.getOrdinal()));
        this.resetTexturePrefix();
    }
    
    public HorseType getType() {
        return HorseType.getArmorType(this.dataManager.get(HORSE_TYPE).intValue());
    }
    
    public void setHorseVariant(int variant) {
        this.dataManager.set(HORSE_VARIANT, Integer.valueOf(variant));
        this.resetTexturePrefix();
    }
    
    public int getHorseVariant() {
        return this.dataManager.get(HORSE_VARIANT).intValue();
    }*/

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : "horse";
    }

    private boolean getHorseWatchableBoolean(int p_110233_1_) {
        return (this.dataManager.get(STATUS).byteValue() & p_110233_1_) != 0;
    }

    private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_) {
        byte b0 = this.dataManager.get(STATUS).byteValue();

        if (p_110208_2_) {
            this.dataManager.set(STATUS, Byte.valueOf((byte) (b0 | p_110208_1_)));
        } else {
            this.dataManager.set(STATUS, Byte.valueOf((byte) (b0 & ~p_110208_1_)));
        }
    }

    @Nullable
    public UUID getOwnerUniqueId() {
        return (UUID) ((Optional) this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerUniqueId(@Nullable UUID uniqueId) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(uniqueId));
    }

    public float getHorseSize() {
        return 0.5F;
    }

    public boolean isHorseJumping() {
        return this.horseJumping;
    }

    public void setHorseTamed(boolean tamed) {
        this.setHorseWatchableBoolean(2, tamed);
    }

    public void setHorseJumping(boolean jumping) {
        this.horseJumping = jumping;
    }

    @Override
    protected void onLeashDistance(float p_142017_1_) {
        if (p_142017_1_ > 6.0F && this.isEatingHaystack()) {
            this.setEatingHaystack(false);
        }
    }

    public boolean isChested() {
        return this.getHorseWatchableBoolean(8);
    }

    public HorseArmorType getHorseArmorType() {
        return HorseArmorType.getByOrdinal(this.dataManager.get(HORSE_ARMOR).intValue());
    }

    public boolean isEatingHaystack() {
        return this.getHorseWatchableBoolean(32);
    }

    public boolean isRearing() {
        return this.getHorseWatchableBoolean(64);
    }

    public boolean isBreeding() {
        return this.getHorseWatchableBoolean(16);
    }

    /**
     * Set horse armor stack (for example: new
     * ItemStack(Items.iron_horse_armor))
     */
    public void setHorseArmorStack(ItemStack itemStackIn) {
        HorseArmorType horsearmortype = HorseArmorType.getByItemStack(itemStackIn);
        this.dataManager.set(HORSE_ARMOR, Integer.valueOf(horsearmortype.getOrdinal()));
        this.resetTexturePrefix();

        if (!this.worldObj.isRemote) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            int i = horsearmortype.getProtection();

            if (i != 0) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(
                        (new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", i, 0)).setSaved(false));
            }
        }
    }

    public void setBreeding(boolean breeding) {
        this.setHorseWatchableBoolean(16, breeding);
    }

    public void setChested(boolean chested) {
        this.setHorseWatchableBoolean(8, chested);
    }

    public void setHorseSaddled(boolean saddled) {
        this.setHorseWatchableBoolean(4, saddled);
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        return this.isBeingRidden() && entity != null && this.isRidingOrBeingRiddenBy(entity) ? false
                : super.attackEntityFrom(source, amount);
    }

    /**
     * Returns true if this entity should push and be pushed by other entities
     * when colliding.
     */
    @Override
    public boolean canBePushed() {
        return !this.isBeingRidden();
    }

    public boolean prepareChunkForSpawn() {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posZ);
        this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, j));
        return true;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (distance > 1.0F) {
            this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F);
        }

        int i = MathHelper.ceiling_float_int((distance * 0.5F - 3.0F) * damageMultiplier);

        if (i > 0) {
            this.attackEntityFrom(DamageSource.fall, i);

            if (this.isBeingRidden()) {
                for (Entity entity : this.getRecursivePassengers()) {
                    entity.attackEntityFrom(DamageSource.fall, i);
                }
            }

            IBlockState iblockstate = this.worldObj
                    .getBlockState(new BlockPos(this.posX, this.posY - 0.2D - this.prevRotationYaw, this.posZ));
            Block block = iblockstate.getBlock();

            if (iblockstate.getMaterial() != Material.AIR && !this.isSilent()) {
                SoundType soundtype = block.getSoundType();
                this.worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, soundtype.getStepSound(),
                        this.getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    /**
     * Returns number of slots depending horse type
     */
    private int getChestSize() {
        return 2;
    }

    /**
     * Updates the items in the saddle and armor slots of the horse's inventory.
     */
    private void updateHorseSlots() {
        if (!this.worldObj.isRemote) {
            this.setHorseSaddled(true);
        }
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never
     * filled.
     */
    @Override
    public void onInventoryChanged(InventoryBasic invBasic) {
        HorseArmorType horsearmortype = this.getHorseArmorType();
        boolean flag = this.isHorseSaddled();
        this.updateHorseSlots();

        if (this.ticksExisted > 20) {
            if (horsearmortype == HorseArmorType.NONE && horsearmortype != this.getHorseArmorType()) {
                this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
            } else if (horsearmortype != this.getHorseArmorType()) {
                this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
            }

            if (!flag && this.isHorseSaddled()) {
                this.playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        this.prepareChunkForSpawn();
        return super.getCanSpawnHere();
    }

    protected EntityHorse getClosestHorse(Entity entityIn, double distance) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;

        for (Entity entity1 : this.worldObj.getEntitiesInAABBexcluding(entityIn,
                entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), IS_HORSE_BREEDING)) {
            double d1 = entity1.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);

            if (d1 < d0) {
                entity = entity1;
                d0 = d1;
            }
        }

        return (EntityHorse) entity;
    }

    public double getHorseJumpStrength() {
        return this.getEntityAttribute(JUMP_STRENGTH).getAttributeValue();
    }

    public boolean isHorseSaddled() {
        return this.getHorseWatchableBoolean(4);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(JUMP_STRENGTH);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22499999403953552D);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 6;
    }

    public int getMaxTemper() {
        return 100;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    /**
     * Get number of ticks, at least during which the living entity will be
     * silent.
     */
    @Override
    public int getTalkInterval() {
        return 400;
    }

    private void resetTexturePrefix() {
        this.texturePrefix = null;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasTexture() {
        return this.hasTexture;
    }

    public void openGUI(EntityPlayer playerEntity) {

    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (stack != null && stack.getItem() == ModItems.spawnShip) {
            this.setTamedBy(player);
            this.worldObj.setEntityState(this, (byte) 7);
            return true;
        }

        if (stack != null && stack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(player, hand, stack);
        } else if (this.isBeingRidden()) {
            return super.processInteract(player, hand, stack);
        } else {
            if (stack != null) {
                HorseArmorType horsearmortype = HorseArmorType.getByItemStack(stack);

                if (horsearmortype != HorseArmorType.NONE) {

                    this.openGUI(player);
                    return true;
                }

                boolean flag = false;
            }

            if (!this.isBeingRidden()) {
                if (stack != null && stack.interactWithEntity(player, this, hand)) {
                    return true;
                } else {
                    this.mountTo(player);
                    return true;
                }
            } else {
                return super.processInteract(player, hand, stack);
            }
        }
    }

    private void mountTo(EntityPlayer player) {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);

        if (!this.worldObj.isRemote) {
            player.startRiding(this);
        }
    }

    /**
     * Dead and sleeping entities cannot move
     */
    @Override
    protected boolean isMovementBlocked() {
        return this.isBeingRidden() && this.isHorseSaddled() ? true : this.isEatingHaystack() || this.isRearing();
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed
     * it (wheat, carrots or seeds depending on the animal type)
     */
    @Override
    public boolean isBreedingItem(@Nullable ItemStack stack) {
        return false;
    }

    /**
     * Called frequently so the entity can update its state every tick as
     * required. For example, zombies and skeletons use this to react to
     * sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {

        super.onLivingUpdate();

        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }

            if (!this.isEatingHaystack() && !this.isBeingRidden() && this.rand.nextInt(300) == 0
                    && this.worldObj
                            .getBlockState(new BlockPos(MathHelper.floor_double(this.posX),
                                    MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ)))
                            .getBlock() == Blocks.GRASS) {
                this.setEatingHaystack(true);
            }

            if (this.isSkeletonTrap() && this.skeletonTrapTime++ >= 18000) {
                this.setDead();
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.worldObj.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.resetTexturePrefix();
        }

        if (this.canPassengerSteer() && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {
            this.jumpRearingCounter = 0;
            this.setRearing(false);
        }

        if (this.sprintCounter > 0) {
            ++this.sprintCounter;

            if (this.sprintCounter > 300) {
                this.sprintCounter = 0;
            }
        }

        this.prevHeadLean = this.headLean;

        if (this.isEatingHaystack()) {
            this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;

            if (this.headLean > 1.0F) {
                this.headLean = 1.0F;
            }
        } else {
            this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;

            if (this.headLean < 0.0F) {
                this.headLean = 0.0F;
            }
        }

        this.prevRearingAmount = this.rearingAmount;

        if (this.isRearing()) {
            this.prevHeadLean = this.headLean = 0.0F;
            this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;

            if (this.rearingAmount > 1.0F) {
                this.rearingAmount = 1.0F;
            }
        } else {
            this.allowStandSliding = false;
            this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount
                    - this.rearingAmount) * 0.6F - 0.05F;

            if (this.rearingAmount < 0.0F) {
                this.rearingAmount = 0.0F;
            }
        }

        this.prevMouthOpenness = this.mouthOpenness;

        if (this.getHorseWatchableBoolean(128)) {
            this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;

            if (this.mouthOpenness > 1.0F) {
                this.mouthOpenness = 1.0F;
            }
        } else {
            this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;

            if (this.mouthOpenness < 0.0F) {
                this.mouthOpenness = 0.0F;
            }
        }
    }

    public void setEatingHaystack(boolean p_110227_1_) {
        this.setHorseWatchableBoolean(32, p_110227_1_);
    }

    public void setRearing(boolean rearing) {
        if (rearing) {
            this.setEatingHaystack(false);
        }

        this.setHorseWatchableBoolean(64, rearing);
    }

    private void makeHorseRear() {
        if (this.canPassengerSteer()) {
            this.jumpRearingCounter = 1;
            this.setRearing(true);
        }
    }

    private void dropItemsInChest(Entity entityIn, AnimalChest animalChestIn) {
        if (animalChestIn != null && !this.worldObj.isRemote) {
            for (int i = 0; i < animalChestIn.getSizeInventory(); ++i) {
                ItemStack itemstack = animalChestIn.getStackInSlot(i);

                if (itemstack != null) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    public boolean setTamedBy(EntityPlayer player) {
        this.setOwnerUniqueId(player.getUniqueID());
        this.setHorseTamed(true);
        return true;
    }

    /**
     * Moves the entity based on the specified heading.
     */
    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        L.s("Move ent. with heading: " + strafe + ", " + forward);
        if (this.isBeingRidden() && this.canBeSteered() && this.isHorseSaddled()) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
            this.prevRotationYaw = this.rotationYaw = entitylivingbase.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            strafe = entitylivingbase.moveStrafing * 0.5F;
            forward = entitylivingbase.moveForward;

            if (forward <= 0.0F) {
                forward *= 0.25F;
                this.gallopTime = 0;
            }

            if (this.onGround && this.jumpPower == 0.0F && this.isRearing() && !this.allowStandSliding) {
                strafe = 0.0F;
                forward = 0.0F;
            }

            if (this.jumpPower > 0.0F && !this.isHorseJumping() && this.onGround) {
                this.motionY = this.getHorseJumpStrength() * this.jumpPower;

                if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                    this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
                }

                this.setHorseJumping(true);
                this.isAirBorne = true;

                if (forward > 0.0F) {
                    float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
                    float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
                    this.motionX += -0.4F * f * this.jumpPower;
                    this.motionZ += 0.4F * f1 * this.jumpPower;
                    this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
                }

                this.jumpPower = 0.0F;
                net.minecraftforge.common.ForgeHooks.onLivingJump(this);
            }

            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed(
                        (float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.moveEntityWithHeading(strafe, forward);
            } else if (entitylivingbase instanceof EntityPlayer) {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            if (this.onGround) {
                this.jumpPower = 0.0F;
                this.setHorseJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

            if (f2 > 1.0F) {
                f2 = 1.0F;
            }

            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(strafe, forward);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("EatingHaystack", this.isEatingHaystack());
        compound.setBoolean("ChestedHorse", this.isChested());
        compound.setBoolean("Bred", this.isBreeding());
        compound.setBoolean("SkeletonTrap", this.isSkeletonTrap());
        compound.setInteger("SkeletonTrapTime", this.skeletonTrapTime);

        if (this.getOwnerUniqueId() != null) {
            compound.setString("OwnerUUID", this.getOwnerUniqueId().toString());
        }


    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setEatingHaystack(compound.getBoolean("EatingHaystack"));
        this.setBreeding(compound.getBoolean("Bred"));
        this.setChested(compound.getBoolean("ChestedHorse"));
        this.setHorseTamed(compound.getBoolean("Tame"));
        this.setSkeletonTrap(compound.getBoolean("SkeletonTrap"));
        this.skeletonTrapTime = compound.getInteger("SkeletonTrapTime");
        String s = "";

        if (compound.hasKey("OwnerUUID", 8)) {
            s = compound.getString("OwnerUUID");
        } else {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            this.setOwnerUniqueId(UUID.fromString(s));
        }

        IAttributeInstance iattributeinstance = this.getAttributeMap().getAttributeInstanceByName("Speed");

        if (iattributeinstance != null) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                    .setBaseValue(iattributeinstance.getBaseValue() * 0.25D);
        }


        this.updateHorseSlots();
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        return false;
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob
     * spawner, natural spawning etc, but not called when entity is reloaded
     * from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        HorseType horsetype = HorseType.HORSE;
        int i = 0;

        if (livingdata instanceof EntityHorse.GroupData) {
            horsetype = ((EntityHorse.GroupData) livingdata).horseType;
            i = ((EntityHorse.GroupData) livingdata).horseVariant & 255 | this.rand.nextInt(5) << 8;
        } else {
            if (this.rand.nextInt(10) == 0) {
                horsetype = HorseType.DONKEY;
            } else {
                int j = this.rand.nextInt(7);
                int k = this.rand.nextInt(5);
                horsetype = HorseType.HORSE;
                i = j | k << 8;
            }

            livingdata = new EntityHorse.GroupData(horsetype, i);
        }

        if (this.rand.nextInt(5) == 0) {
            this.setGrowingAge(-24000);
        }

        if (horsetype.isUndead()) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getModifiedMaxHealth());

            if (horsetype == HorseType.HORSE) {
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                        .setBaseValue(this.getModifiedMovementSpeed());
            } else {
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.17499999701976776D);
            }
        }

        if (horsetype.hasMuleEars()) {
            this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(0.5D);
        } else {
            this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
        }

        this.setHealth(this.getMaxHealth());
        return livingdata;
    }

    /**
     * returns true if all the conditions for steering the entity are met. For
     * pigs, this is true if it is being ridden by a player and the player is
     * holding a carrot-on-a-stick
     */
    @Override
    public boolean canBeSteered() {
        Entity entity = this.getControllingPassenger();
        return entity instanceof EntityLivingBase;
    }

    @SideOnly(Side.CLIENT)
    public float getGrassEatingAmount(float p_110258_1_) {
        return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
    }

    @SideOnly(Side.CLIENT)
    public float getRearingAmount(float p_110223_1_) {
        return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
    }

    @SideOnly(Side.CLIENT)
    public float getMouthOpennessAngle(float p_110201_1_) {
        return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setJumpPower(int jumpPowerIn) {
        if (this.isHorseSaddled()) {
            if (jumpPowerIn < 0) {
                jumpPowerIn = 0;
            } else {
                this.allowStandSliding = true;
                this.makeHorseRear();
            }

            if (jumpPowerIn >= 90) {
                this.jumpPower = 1.0F;
            } else {
                this.jumpPower = 0.4F + 0.4F * jumpPowerIn / 90.0F;
            }
        }
    }

    @Override
    public boolean canJump() {
        return this.isHorseSaddled();
    }

    @Override
    public void handleStartJump(int p_184775_1_) {
        this.allowStandSliding = true;
        this.makeHorseRear();
    }

    @Override
    public void handleStopJump() {
    }

    /**
     * "Spawns particles for the horse entity. par1 tells whether to spawn
     * hearts. If it is false, it spawns smoke."
     */
    @SideOnly(Side.CLIENT)
    protected void spawnHorseParticles(boolean p_110216_1_) {
        EnumParticleTypes enumparticletypes = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;

        for (int i = 0; i < 7; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(enumparticletypes,
                    this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                    this.posY + 0.5D + this.rand.nextFloat() * this.height,
                    this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 7) {
            this.spawnHorseParticles(true);
        } else if (id == 6) {
            this.spawnHorseParticles(false);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);

        if (passenger instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }

        if (this.prevRearingAmount > 0.0F) {
            float f3 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float f1 = 0.7F * this.prevRearingAmount;
            float f2 = 0.15F * this.prevRearingAmount;
            passenger.setPosition(this.posX + f1 * f3,
                    this.posY + this.getMountedYOffset() + passenger.getYOffset() + f2, this.posZ - f1 * f);

            if (passenger instanceof EntityLivingBase) {
                ((EntityLivingBase) passenger).renderYawOffset = this.renderYawOffset;
            }
        }
    }

    /**
     * Returns randomized max health
     */
    private float getModifiedMaxHealth() {
        return 15.0F + this.rand.nextInt(8) + this.rand.nextInt(9);
    }

    /**
     * Returns randomized jump strength
     */
    private double getModifiedJumpStrength() {
        return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D
                + this.rand.nextDouble() * 0.2D;
    }

    /**
     * Returns randomized movement speed
     */
    private double getModifiedMovementSpeed() {
        return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D
                + this.rand.nextDouble() * 0.3D) * 0.25D;
    }

    public boolean isSkeletonTrap() {
        return this.skeletonTrap;
    }

    public void setSkeletonTrap(boolean skeletonTrapIn) {

    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return this.height;
    }

    /**
     * For vehicles, the first passenger is generally considered the controller
     * and "drives" the vehicle. For example, Pigs, Horses, and Boats are
     * generally "steered" by the controlling passenger.
     */
    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public static class GroupData implements IEntityLivingData {
        public HorseType horseType;
        public int horseVariant;

        public GroupData(HorseType p_i46589_1_, int p_i46589_2_) {
            this.horseType = p_i46589_1_;
            this.horseVariant = p_i46589_2_;
        }
    }

    // FORGE
    private net.minecraftforge.items.IItemHandler itemHandler = null; // Initialized
                                                                      // by
                                                                      // initHorseChest
                                                                      // above.

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability,
            net.minecraft.util.EnumFacing facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) itemHandler;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability,
            net.minecraft.util.EnumFacing facing) {
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return this;
    }
}
