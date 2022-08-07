package dev.tmp.StareTillTheyGrow;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.EventHandlers.PlayerLeaveEventHandler;
import dev.tmp.StareTillTheyGrow.EventHandlers.ServerTickEventHandler;
import dev.tmp.StareTillTheyGrow.Network.Network;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        forgeEventBus.register( new PlayerLeaveEventHandler() );

        // Initialize the Network class.
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetupEventHandler);
    }

    private void commonSetupEventHandler(FMLCommonSetupEvent event) {
        Network.initialize();
    }
}


