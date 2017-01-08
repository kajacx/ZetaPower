package com.hrkalk.zetapower.entities.vessel;

import java.io.IOException;

import javax.annotation.Nullable;

import com.hrkalk.zetapower.client.input.InputHandler;
import com.hrkalk.zetapower.client.render.vessel.ScaledRotator;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.MathUtils;
import com.hrkalk.zetapower.utils.NBTReader;
import com.hrkalk.zetapower.utils.Vector3d;
import com.hrkalk.zetapower.vessel.BlockCluster;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.FMLClientHandler;

public class VesselEntity_Reload {

    private boolean goUp = true; //always go Y+ only when holding the up key
    private boolean goForw = false; //move horizontaly only when holding the horizontal keys 

    private Vector3d lookForw = new Vector3d();
    private Vector3d lookUp = new Vector3d();
    private Vector3d lookRight = new Vector3d();

    //look vectors affected by movement options
    //normalized, not affected by speed
    private Vector3d movForw = new Vector3d();
    private Vector3d movUp = new Vector3d();
    private Vector3d movRight = new Vector3d();

    private Vector3d tmp = new Vector3d();

    public VesselEntity thiz;

    private boolean useOldControls = false;

    public VesselEntity_Reload() {
        goUp |= goForw;
    }

    public void onUpdate() {
        //L.d("On update");
        //this.super_onUpdate();
        if (thiz == null) {
            L.d("thiz cannot be null");
            return;
        }
        if (thiz.isBeingRidden()) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) thiz.getControllingPassenger();

            moveEntity(entityLivingBase);
        }
    }

    public void moveEntity(EntityLivingBase entityLivingBase) {

        if (thiz.worldObj.isRemote) {
            double moveSpeed = 10 / 20d;
            float rotSpeed = 0.01f;

            if (useOldControls) {
                double rotationXZ = entityLivingBase.rotationYaw * MathUtils.degToRadF;
                double rotationY = entityLivingBase.rotationPitch * MathUtils.degToRadF;

                thiz.rotationYaw = entityLivingBase.rotationYaw;
                thiz.rotationPitch = entityLivingBase.rotationPitch;

                fillLookVectorsOld(rotationXZ, rotationY);
                fillMoveVectorsOld(rotationXZ, rotationY);
            } else {
                //use new controls with scaled rotator
                rotateShipFromInput(rotSpeed);
                fillMoveVectors();
            }
            scaleMoveVectors();

            tmp.set(movForw).add(movUp).add(movRight).normalise().scale(moveSpeed);

            //L.d("Forw: " + movForw);
            //L.d("Up: " + movUp);
            //L.d("Right: " + movRight);
            //L.d("Result: " + tmp);//*/

            thiz.moveEntity(tmp.x, tmp.y, tmp.z);
        }
    }

    private void rotateShipFromInput(float rotSpeed) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        //GameSettings settings = mc.gameSettings;

        float angle = 0;
        if (InputHandler.vesselRotateCW.isKeyDown())
            angle += 1;
        if (InputHandler.vesselRotateCCW.isKeyDown())
            angle -= 1;


        ScaledRotator rotator = thiz.cluster.getRotator();
        if (rotator != null) {
            rotator.rotateLookForward(angle * rotSpeed);
            //L.d("rot up: " + rotator.lookUp);
            //L.s("rotator: ");
        }
    }

    private void fillMoveVectors() {
        ScaledRotator rotator = thiz.cluster.getRotator();
        if (rotator != null) {
            movForw.set(rotator.lookForw.x, rotator.lookForw.y, rotator.lookForw.z);
            movUp.set(rotator.lookUp.x, rotator.lookUp.y, rotator.lookUp.z);
            movRight.set(rotator.lookRight.x, rotator.lookRight.y, rotator.lookRight.z);
        }
    }

    private void fillLookVectorsOld(double rotationXZ, double rotationY) {
        double x, y, z;
        double cosY = Math.cos(rotationY);

        //forward
        x = cosY * -Math.sin(rotationXZ);
        y = -Math.sin(rotationY);
        z = cosY * Math.cos(rotationXZ);
        lookForw.set(x, y, z);

        //right
        x = -Math.cos(rotationXZ);
        y = 0;
        z = -Math.sin(rotationXZ);
        lookRight.set(x, y, z);

        //up
        Vector3d.cross(lookRight, lookForw, lookUp);

        /* L.d("Forw: " + lookForw);
        L.d("Up: " + lookUp);
        L.d("Right: " + lookRight);
        L.d("Lengths: " + lookForw.lengthSquared() + ", " + lookUp.lengthSquared() + ", " + lookRight.lengthSquared());*/
    }

    private void fillMoveVectorsOld(double rotationXZ, double rotationY) {
        if (goForw) {
            double x, y, z;
            x = -Math.sin(rotationXZ);
            y = 0;
            z = Math.cos(rotationXZ);
            movForw.set(x, y, z);
        } else {
            movForw.set(lookForw);
        }

        if (goUp) {
            movUp.set(0, 1, 0);
        } else {
            movUp.set(lookUp);
        }

        movRight.set(lookRight);
    }

    private void scaleMoveVectors() {
        Minecraft mc = FMLClientHandler.instance().getClient();
        GameSettings settings = mc.gameSettings;

        double forward = 0;
        double up = 0;
        double right = 0;

        forward += settings.keyBindForward.isKeyDown() ? 1 : 0;
        forward -= settings.keyBindBack.isKeyDown() ? 1 : 0;

        up += settings.keyBindJump.isKeyDown() ? 1 : 0;
        up -= settings.keyBindSprint.isKeyDown() ? 1 : 0;

        right += settings.keyBindRight.isKeyDown() ? 1 : 0;
        right -= settings.keyBindLeft.isKeyDown() ? 1 : 0;

        movForw.scale(forward);
        movUp.scale(up);
        movRight.scale(right);
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        //L.d("Ship save to NBT");

        compound.setDouble("posX", thiz.posX);
        compound.setDouble("posY", thiz.posY);
        compound.setDouble("posZ", thiz.posZ);

        if (thiz.cluster != null) {
            compound.setTag("cluster", thiz.cluster.saveToNBT(new NBTTagCompound()));
        } else {
            L.w("Cluster is null when writing, this shouldn't happen");
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        //L.d("Ship read from NBT");

        //thiz.posX = NBTReader.readDoubleOr(compound, "posX", 0);
        //thiz.posY = NBTReader.readDoubleOr(compound, "posY", 0);
        //thiz.posZ = NBTReader.readDoubleOr(compound, "posZ", 0);

        NBTTagCompound clusterTag = NBTReader.readTagOr(compound, "cluster", null);
        if (clusterTag != null) {
            thiz.cluster = new BlockCluster(ZetaDimensionHandler.getMallocWorld(), clusterTag);
        } else {
            L.w("Cluster is null when reading. This shouldn't happen");
        }
    }

    public void writeSpawnData(ByteBuf buffer) {
        L.d("writeSpawnData");

        NBTTagCompound tag = new NBTTagCompound();
        writeEntityToNBT(tag);
        new PacketBuffer(buffer).writeNBTTagCompoundToBuffer(tag);
    }

    public void readSpawnData(ByteBuf additionalData) {
        L.d("readSpawnData");
        try {

            NBTTagCompound tag = new NBTTagCompound();
            try {
                tag = new PacketBuffer(additionalData).readNBTTagCompoundFromBuffer();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
            readEntityFromNBT(tag);
        } catch (ArrayIndexOutOfBoundsException ex) {
            L.e("Error when reading dpawn data: " + ex.getMessage());
        }
    }

    public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
        L.d("interact");

        if (!thiz.worldObj.isRemote && stack != null && stack.getItem() == Items.DIAMOND) {
            thiz.worldObj.removeEntity(thiz);
            return true;
        } else if (!thiz.worldObj.isRemote && !thiz.isBeingRidden()) {
            player.startRiding(thiz);
            return true;
        } else {
            return false;
        }

    }

}
