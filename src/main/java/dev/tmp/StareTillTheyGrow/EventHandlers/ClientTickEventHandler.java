package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.StareTillTheyGrow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = StareTillTheyGrow.MOD_ID, value = Dist.CLIENT)
public final class ClientTickEventHandler {
    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        System.out.println("Client Tick");
    }
}
