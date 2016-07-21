package com.hrkalk.zetapower.client.render.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class RideableShipModel extends ModelBase {

    ModelRenderer render = new ModelRenderer(this, 0, 0).setTextureSize(16, 16);

    public RideableShipModel(RideableShipRenderer thiz) {
        render.addBox(-8, 0, -8, 16, 16, 16);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        render.render(scale);
    }

}
