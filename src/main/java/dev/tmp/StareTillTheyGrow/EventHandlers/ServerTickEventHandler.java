package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Actions.Block.ApplyBoneMealAction;
import dev.tmp.StareTillTheyGrow.Actions.Block.RegrowCakeAction;
import dev.tmp.StareTillTheyGrow.Actions.Entity.FallInLoveAction;
import dev.tmp.StareTillTheyGrow.Actions.Entity.GrowUpAction;
import dev.tmp.StareTillTheyGrow.Actions.Entity.RegrowWoolAction;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ServerTickEventHandler {
    @SubscribeEvent
    public void serverTickHandler(TickEvent.ServerTickEvent event) {
        if ( event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END ) {
            PlayerTargetDictionary.forEach((player, playerTarget) -> {
                playerTarget.tick();
                if (playerTarget.canInvoke()) {
                    if (playerTarget instanceof PlayerBlockTarget playerBlockTarget) {
                        (new ApplyBoneMealAction(playerBlockTarget)).invoke();
                        (new RegrowCakeAction(playerBlockTarget)).invoke();
                    } else if (playerTarget instanceof PlayerEntityTarget playerEntityTarget) {
                        (new FallInLoveAction(playerEntityTarget)).invoke();
                        (new GrowUpAction(playerEntityTarget)).invoke();
                        (new RegrowWoolAction(playerEntityTarget)).invoke();
                    }
                }
            });
        }
    }
}
