package com.hrkalk.zetapower.client.render.entities;

import com.hrkalk.zetapower.client.render.entities.RideableShipRendererReload.MyModel;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RideableShipRenderer_Reload {

    public RideableShipRenderer thiz;

    private MyModel model;
    private ResourceLocation texture = new ResourceLocation("textures/entities/rideable_ship_plate.png.png");

    {
        model = new MyModel(thiz);
    }

    public ResourceLocation getEntityTexture(Entity arg1) {
        L.d("spam 2");
        return texture;
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        L.s("somethuing");
        thiz.call_bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        model.render(entity, 0, 0, 0, 0, 0, 0);
        GlStateManager.popMatrix();
    }

}
