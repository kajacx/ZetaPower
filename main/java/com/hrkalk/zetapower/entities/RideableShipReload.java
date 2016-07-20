package com.hrkalk.zetapower.entities;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.client.FMLClientHandler;

public class RideableShipReload {
    //look vectors affected by movement options
    private Vector3f movForw = new Vector3f();
    private Vector3f movUp = new Vector3f();
    private Vector3f movRight = new Vector3f();

    public void moveEntity(RideableShip thiz, EntityLivingBase entitylivingbase) {
        if (thiz.worldObj.isRemote) {
            Minecraft mc = FMLClientHandler.instance().getClient();
            GameSettings settings = mc.gameSettings;

            float forward = 0;
            float up = 0;
            float right = 0;

            forward += settings.keyBindForward.isKeyDown() ? 1 : 0;
            forward -= settings.keyBindBack.isKeyDown() ? 1 : 0;

            up += settings.keyBindJump.isKeyDown() ? 1 : 0;
            up -= settings.keyBindSprint.isKeyDown() ? 1 : 0;

            right += settings.keyBindRight.isKeyDown() ? 1 : 0;
            right -= settings.keyBindLeft.isKeyDown() ? 1 : 0;

            double speed = 1 / 20d;
            double rotationXZ = entitylivingbase.rotationYaw * MathUtils.degToRadF;
            double rotationY = entitylivingbase.rotationPitch * MathUtils.degToRadF;

            //L.s("Rotation XZ:" + rotationXZ);
            //L.s("Rotation Y:" + rotationY);
            //L.s("Rotation head:" + entitylivingbase.rotationYawHead);
            double cosY = Math.cos(rotationY);

            double y = -Math.sin(rotationY) * speed;
            double x = cosY * -Math.sin(rotationXZ) * speed;
            double z = cosY * Math.cos(rotationXZ) * speed;

            thiz.moveEntity(x, y, z);
        }
    }
}
