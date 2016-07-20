package com.hrkalk.zetapower.client.render.entities;

import com.hrkalk.zetapower.entities.RideableShip_Backup;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RideableShipRenderer extends RenderLiving<RideableShip_Backup> {

    private static final ResourceLocation pigTextures = new ResourceLocation("minecraft",
            "textures/entity/pig/pig.png");

    public RideableShipRenderer(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(RideableShip_Backup entity) {
        return pigTextures;
    }

}
