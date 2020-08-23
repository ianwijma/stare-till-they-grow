package dev.tmp.StaringMod.Config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TimingConfig {

    public static ForgeConfigSpec.IntValue delay;
    public static ForgeConfigSpec.IntValue everyXSeconds;

    public static void init ( ForgeConfigSpec.Builder server ) {
        server.comment("Delay and apply rate");

        delay = server
                .comment("The delay in seconds before we start applying bonemeal")
                .defineInRange("timing.delay", 2, 1, 60);

        everyXSeconds = server
                .comment("The time in seconds between each applying of bonemeal")
                .defineInRange("timing.everyXSeconds", 1, 1, 60);
    }

}
