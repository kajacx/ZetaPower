package com.hrkalk.zetapower.event;

import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerCommon {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerPickupXP(PlayerPickupXpEvent e) {
        /*Log.d("Entity: " + e.entity);
        Log.d("Living: " + e.entityLiving);
        Log.d("Player: " + e.entityPlayer);
        e.orb.xpValue *= 2;*/
    }

}
