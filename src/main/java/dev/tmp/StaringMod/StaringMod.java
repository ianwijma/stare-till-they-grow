package dev.tmp.StaringMod;

import dev.tmp.StaringMod.EventHandlers.ServerTickEventHandlers;
import dev.tmp.StaringMod.Network.Network;
import dev.tmp.StaringMod.EventHandlers.PlayerEventHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("staringmod")
public class StaringMod
{

    public static String MODE_ID = "staringmod";

    public StaringMod() {
        // Register listeners to minecraftEvetns
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener( this::commonSetup );

        // Register events handlers against the Minecraft Forge events bus
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register( new PlayerEventHandlers() );
        forgeEventBus.register( new ServerTickEventHandlers() );
    }

    private void commonSetup ( FMLCommonSetupEvent event ) {
        // Setup the Client to Server communication layer
        Network.init();
    }

}
