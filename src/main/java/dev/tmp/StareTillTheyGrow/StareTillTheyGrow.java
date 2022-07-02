package dev.tmp.StareTillTheyGrow;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.EventHandlers.ServerTickEventHandler;
import dev.tmp.StareTillTheyGrow.Network.Network;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// TODO: [network] Cleaner communication between server and client
// TODO: [actions] More abstract handling of the server and client ticks
// TODO: [actions] Tick based action instead of time based
// TODO: [particles] Particles are not centered - Maybe custom particles
// TODO: [config] More organised config
// TODO: [config] White/blacklist support for mods
// TODO: [config] White/blacklist support for block groups
// TODO: [config] White/blacklist support for forge groups (crops for example)
// TODO: [config/actions] Configurable delays for each mod / group / action

// TODO: [research] Shifting enables alternative moves

@Mod("staretilltheygrow")
public class StareTillTheyGrow
{
    public static final String MOD_ID = "staretilltheygrow";

    public StareTillTheyGrow() {
        // Load the config
        ModLoadingContext.get().registerConfig( ModConfig.Type.COMMON, Config.COMMON_SPEC);

        // Register the server tick handler
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register( new ServerTickEventHandler() );

        // Initialize the Network class.
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetupEventHandler);
    }

    private void commonSetupEventHandler(FMLCommonSetupEvent event) {
        Network.initialize();
    }
}


