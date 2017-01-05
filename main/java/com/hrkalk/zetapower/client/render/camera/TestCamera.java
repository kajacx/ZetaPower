package com.hrkalk.zetapower.client.render.camera;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.client.render.vessel.ScaledRotator;
import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.renderer.ccc.IPlayerCamera;
import net.minecraft.entity.Entity;

public class TestCamera implements IPlayerCamera {
    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f look = new Vector3f(1, 0, 0);
    private Vector3f lookUp = new Vector3f(0, 1, 0);

    private long milisAtStart;

    ScaledRotator rotator = new ScaledRotator(MathUtils.vectorZNeg);

    public TestCamera() {
        milisAtStart = System.currentTimeMillis();
    }

    @Override
    public void update(Entity entity, float partialTicks) {
        //time measurement
        float time = System.currentTimeMillis() - milisAtStart;
        time /= 1000;
        time /= 2; //more slow down

        //entity position
        double entityX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double entityY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        double entityZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

        rotator.resetRotation();
        rotator.rotateLookUp(time);
        rotator.rotateLookRight(-30 * MathUtils.degToRadF);

        look.set(rotator.getLookForw());
        lookUp.set(rotator.getLookUp());

        //L.d("Forw: " + look);
        //L.d("Up: " + lookUp);

        //position.set((float) entityX - 4 * look.x, (float) entityY + entity.getEyeHeight() - 4 * look.y, (float) entityZ - 4 * look.z);
        position.set((float) entityX, (float) entityY + entity.getEyeHeight() + 1, (float) entityZ);
    }

    @Override
    public Vector3f getCameraPosition() {
        return position;
    }

    @Override
    public Vector3f getLookVector() {
        return look;
    }

    @Override
    public Vector3f getLookUpVector() {
        return lookUp;
    }

    @Override
    public boolean isFirstPerson() {
        return true;
    }

    @Override
    public void onSelected() {
        System.out.println("My camera selected");
    }

    @Override
    public void onDeselected() {
        System.out.println("My camera deselected");

    }

}
