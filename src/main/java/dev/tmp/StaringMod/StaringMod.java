package dev.tmp.StaringMod;

import dev.tmp.StaringMod.Config.Config;
import dev.tmp.StaringMod.EventHandlers.ServerTickEventHandlers;
import dev.tmp.StaringMod.Network.Network;
import dev.tmp.StaringMod.EventHandlers.PlayerEventHandlers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("staringmod")
public class StaringMod
{

    public static String MODE_ID = "staringmod";
    public static final Logger LOGGER = LogManager.getLogger(MODE_ID);


    public StaringMod() {
        // Load the config
        ModLoadingContext.get().registerConfig( ModConfig.Type.SERVER, Config.CONFIG);
        Config.loadConfig( Config.CONFIG, FMLPaths.CONFIGDIR.get().resolve( MODE_ID + ".toml" ).toString() );

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
