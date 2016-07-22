package com.hrkalk.zetapower.event;

import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerTET {

    public static boolean preventDropDueToTET = false;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBlockHarvest(HarvestDropsEvent event) {
        //L.d("kappa");
        if (preventDropDueToTET) {
            event.getDrops().clear(); // remove vanilla drops
        }
    }
}
