package com.hrkalk.zetapower.client.render.camera;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.renderer.ccc.IPlayerCamera;
import net.minecraft.entity.Entity;

public class TestCamera implements IPlayerCamera {
    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f look = new Vector3f(1, 0, 0);
    private Vector3f lookUp = new Vector3f(0, 1, 0);

    @Override
    public void update(Entity player, float partialTicks) {
        position.set((float) player.posX, (float) player.posY, (float) player.posZ);
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
