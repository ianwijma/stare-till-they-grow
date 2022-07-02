package dev.tmp.StareTillTheyGrow.EventHandlers;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ServerTickEventHandler {
    @SubscribeEvent
    public void serverTickHandler(TickEvent.ServerTickEvent event) {

        System.out.println("Server Tick");
    }
}
