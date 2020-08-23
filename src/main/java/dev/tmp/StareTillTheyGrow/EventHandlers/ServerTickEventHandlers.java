package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Library.GrowableBlockDictionary;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerTickEventHandlers {

    @SubscribeEvent
    public void lookedAtGrowableEvent ( TickEvent.ServerTickEvent event ) {
        if ( event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END ) {
            //
            GrowableBlockDictionary.forEach((__, worldBlockPos) -> worldBlockPos.checkGrowth());
        }
    }

}
