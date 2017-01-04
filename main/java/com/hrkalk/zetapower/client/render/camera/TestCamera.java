package com.hrkalk.zetapower.client.render.camera;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.renderer.ccc.IPlayerCamera;
import net.minecraft.entity.Entity;

public class TestCamera implements IPlayerCamera {
    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f look = new Vector3f(1, 0, 0);
    private Vector3f lookUp = new Vector3f(0, 1, 0);

    private long milisAtStart;

    public TestCamera() {
        milisAtStart = System.currentTimeMillis();
    }

    @Override
    public void update(Entity entity, float partialTicks) {
        //entity position
        double entityX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double entityY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        double entityZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

        position.set((float) (entityX + 2), (float) entityY + entity.getEyeHeight(), (float) entityZ);

        float time = System.currentTimeMillis() - milisAtStart;
        time /= 1000;

        look.x = (float) Math.cos(time);
        look.z = (float) Math.sin(time);
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
