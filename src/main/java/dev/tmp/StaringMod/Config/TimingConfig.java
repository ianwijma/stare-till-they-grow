package dev.tmp.StaringMod.Config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TimingConfig {

    public static ForgeConfigSpec.IntValue delay;
    public static ForgeConfigSpec.IntValue everyXSeconds;

    public static void init ( ForgeConfigSpec.Builder server ) {
        server.comment("Timings");

        delay = server
                .comment("The delay")
                .defineInRange("timing.delay", 2, 1, 60);

        everyXSeconds = server
                .comment("How often it allows it to grow")
                .defineInRange("timing.everyXSeconds", 1, 1, 60);
    }

}
