package com.hrkalk.zetapower.entities;

import com.hrkalk.zetapower.Main;

import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    public static void init() {
        EntityRegistry.registerModEntity(RideableShip.class, "ridable_ship", 0, Main.instance, 80, 3, true);
        EntityRegistry.registerEgg(RideableShip.class, 0xFF8000, 0xB04040);
    }
}
