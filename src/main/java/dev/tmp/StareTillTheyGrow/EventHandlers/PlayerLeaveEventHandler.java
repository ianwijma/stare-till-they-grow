package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerLeaveEventHandler {
    @SubscribeEvent
    public void playerLeftHandler(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerTargetDictionary.unregister(player);
        }
    }
}
