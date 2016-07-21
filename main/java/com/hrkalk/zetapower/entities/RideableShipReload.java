package com.hrkalk.zetapower.entities;

import com.hrkalk.zetapower.utils.MathUtils;
import com.hrkalk.zetapower.utils.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.client.FMLClientHandler;

public class RideableShipReload {
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

    public RideableShipReload() {
        goUp |= goForw;
    }

    public void moveEntity(RideableShip thiz, EntityLivingBase entitylivingbase) {

        if (thiz.worldObj.isRemote) {
            double speed = 10 / 20d;
            double rotationXZ = entitylivingbase.rotationYaw * MathUtils.degToRadF;
            double rotationY = entitylivingbase.rotationPitch * MathUtils.degToRadF;

            thiz.rotationYaw = entitylivingbase.rotationYaw;
            thiz.rotationPitch = entitylivingbase.rotationPitch;

            fillLookVectors(rotationXZ, rotationY);
            fillMoveVectors(rotationXZ, rotationY);
            scaleMoveVectors();

            tmp.set(movForw).add(movUp).add(movRight).normalise().scale(speed);

            //L.d("Forw: " + movForw);
            //L.d("Up: " + movUp);
            //L.d("Right: " + movRight);
            //L.d("Result: " + tmp);//*/

            thiz.moveEntity(tmp.x, tmp.y, tmp.z);
        }
    }

    private void fillLookVectors(double rotationXZ, double rotationY) {
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

    private void fillMoveVectors(double rotationXZ, double rotationY) {
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
}
