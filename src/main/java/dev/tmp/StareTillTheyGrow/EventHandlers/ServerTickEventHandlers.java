package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Library.PlayerTargetDictionary;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerTickEventHandlers {

    @SubscribeEvent
    public void handlePlayerTarget ( TickEvent.ServerTickEvent event ) {
        if ( event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END ) {
            PlayerTargetDictionary.forEach((__, playerTarget) -> playerTarget.handle());
        }
    }

}
