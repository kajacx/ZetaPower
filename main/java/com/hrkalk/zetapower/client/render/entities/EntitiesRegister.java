package com.hrkalk.zetapower.client.render.entities;

import com.hrkalk.zetapower.entities.RideableShip;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntitiesRegister {
    public static void preInit() {
        //RenderingRegistry.registerEntityRenderingHandler(RideableShip.class, RideableShipRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RideableShip.class, (manager -> new RideableShipRenderer(manager)));
    }
}
