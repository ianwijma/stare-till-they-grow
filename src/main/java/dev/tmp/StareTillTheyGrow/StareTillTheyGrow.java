package dev.tmp.StareTillTheyGrow;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.EventHandlers.ServerTickEventHandlers;
import dev.tmp.StareTillTheyGrow.Network.Network;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("staretilltheygrow")
public class StareTillTheyGrow
{

    public static final String MOD_ID = "staretilltheygrow";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public StareTillTheyGrow() {
        // Load the config
        ModLoadingContext.get().registerConfig( ModConfig.Type.COMMON, Config.COMMON_SPEC);

        // Register listeners to minecraftEvetns
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener( this::commonSetup );

        // Register events handlers against the Minecraft Forge events bus
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register( new ServerTickEventHandlers() );
    }

    private void commonSetup ( FMLCommonSetupEvent event ) {
        // Setup the Client to Server communication layer
        Network.init();
    }

}
