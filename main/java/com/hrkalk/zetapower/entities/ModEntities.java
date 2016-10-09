package com.hrkalk.zetapower.entities;

import com.hrkalk.zetapower.Main;
import com.hrkalk.zetapower.entities.vessel.VesselEntity;

import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    public static void init() {
        EntityRegistry.registerModEntity(VesselEntity.class, "rideable_ship", 0, Main.instance, 80, 3, true);
        EntityRegistry.registerEgg(VesselEntity.class, 0xFF8000, 0xB04040);
    }
}
