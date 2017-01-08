package com.hrkalk.zetapower.client.render.camera;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.client.render.vessel.ScaledRotator;
import com.hrkalk.zetapower.entities.vessel.VesselEntity;
import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.renderer.ccc.IPlayerCamera;
import net.minecraft.entity.Entity;

public class VesselCamera implements IPlayerCamera {

    private float distBehind = 10;
    private float distUp = 4;

    private Vector3f lookForward = new Vector3f(1, 0, 0);
    private Vector3f lookUp = new Vector3f(0, 1, 0);
    private Vector3f cameraPosition = new Vector3f(0, 80, 0);

    private IPlayerCamera whenNotMounted;
    private boolean isMounted = false;

    public VesselCamera(IPlayerCamera whenNotMounted) {
        this.whenNotMounted = whenNotMounted;
    }

    @Override
    public void update(Entity player, float partialTicks) {
        Entity entity = player.getRidingEntity();
        if (entity instanceof VesselEntity) {
            VesselEntity vessel = (VesselEntity) entity;
            ScaledRotator rotator = vessel.cluster.getRotator();

            MathUtils.interpolate(vessel.prevLookForw, rotator.lookForw, partialTicks, lookForward);
            MathUtils.interpolate(vessel.prevLookUp, rotator.lookUp, partialTicks, lookUp);

            float posX = (float) (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks);
            float posY = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks);
            float posZ = (float) (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks);

            posX += -distBehind * lookForward.x + distUp * lookUp.x;
            posY += -distBehind * lookForward.y + distUp * lookUp.y;
            posZ += -distBehind * lookForward.z + distUp * lookUp.z;

            cameraPosition.set(posX, posY, posZ);
            isMounted = true;
        } else {
            whenNotMounted.update(player, partialTicks);
            lookForward.set(whenNotMounted.getLookVector());
            lookUp.set(whenNotMounted.getLookUpVector());
            cameraPosition.set(whenNotMounted.getCameraPosition());
            isMounted = false;
        }
    }

    @Override
    public boolean isFirstPerson() {
        if (isMounted)
            return false;
        return whenNotMounted.isFirstPerson();
    }

    @Override
    public Vector3f getCameraPosition() {
        return cameraPosition;
    }

    @Override
    public Vector3f getLookVector() {
        return lookForward;
    }

    @Override
    public Vector3f getLookUpVector() {
        return lookUp;
    }

    @Override
    public void onSelected() {
        whenNotMounted.onSelected();
    }

    @Override
    public void onDeselected() {
        whenNotMounted.onDeselected();
    }

}
