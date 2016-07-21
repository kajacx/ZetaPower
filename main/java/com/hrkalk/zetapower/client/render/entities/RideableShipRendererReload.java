package com.hrkalk.zetapower.client.render.entities;

import com.hrkalk.zetapower.entities.RideableShip;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RideableShipRendererReload {

    private MyModel model;
    private ResourceLocation texture = new ResourceLocation("textures/entities/rideable_ship_plate.png.png");

    public ResourceLocation getEntityTexture(RideableShipRenderer thiz, RideableShip entity) {
        L.d("spam 2");
        return texture;
    }

    public void init(RideableShipRenderer thiz) {
        if (model == null) {
            model = new MyModel(thiz);
        }
    }

    public void doRender(RideableShipRenderer thiz, RideableShip entity, double x, double y, double z, float entityYaw, float partialTicks) {
        init(thiz);
        //thiz.bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        model.render(entity, 0, 0, 0, 0, 0, 0);
        GlStateManager.popMatrix();
    }

    static class MyModel extends ModelBase {
        ModelRenderer render = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);

        public MyModel(RideableShipRenderer thiz) {
            // TODO Auto-generated constructor stub
            L.d("FFFFF");
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            render.render(scale);
        }

    }

    /* @Override
    protected ResourceLocation getEntityTexture(RideableShip entity) {
        // TODO Auto-generated method stub
        return null;
    }*/
}
