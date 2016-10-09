package com.hrkalk.zetapower.client.render.entities;

import com.hrkalk.zetapower.entities.vessel.VesselEntity;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntitiesRegister {
    @SuppressWarnings("unchecked")
    public static void preInit() {
        //RenderingRegistry.registerEntityRenderingHandler(RideableShip.class, RideableShipRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(VesselEntity.class, (manager -> new VesselEntityRenderer(manager)));
    }
}
