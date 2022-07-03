package dev.tmp.StareTillTheyGrow.Config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber
public class Config {

    public static class Client {
        public Client ( ForgeConfigSpec.Builder builder ) {
            // Unused for now
        }
    }

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Common {
        // General
        public ForgeConfigSpec.BooleanValue enableApplyBoneMeal;
        public ForgeConfigSpec.BooleanValue enableFallInLove;
        public ForgeConfigSpec.BooleanValue enableGrowUp;
        public ForgeConfigSpec.BooleanValue enableRegrowCake;
        public ForgeConfigSpec.BooleanValue enableRegrowWool;

        // Timings
        public ForgeConfigSpec.IntValue ticksDelay;
        public ForgeConfigSpec.IntValue ticksBetween;


        // Build the common config
        public Common ( ForgeConfigSpec.Builder builder ) {
            builder.comment("General Settings").push("general");
            enableApplyBoneMeal = builder
                    .comment("Enable staring at crops, saplings and alike to make them grow!")
                    .define("general.enableApplyBoneMeal", true);
            enableFallInLove = builder
                    .comment("Enable staring Animals to make them fall in love!")
                    .define("general.enableFallInLove", true);
            enableGrowUp = builder
                    .comment("Enable staring baby animals to make them grow up!")
                    .define("general.enableGrowUp", true);
            enableRegrowCake = builder
                    .comment("Enable staring at cake, because it isn't a lie!")
                    .define("general.enableRegrowCake", true);
            enableRegrowWool = builder
                    .comment("Enable staring at sheared sheep to make them regrow their wool!")
                    .define("general.enableRegrowWool", true);
            builder.pop();

            builder.comment("Timing Settings").push("timing");
            ticksDelay = builder
                    .comment("The amount of ticks before we start trigger the actions")
                    .defineInRange("timing.ticksDelay", 40, 1, Integer.MAX_VALUE);
            ticksBetween = builder
                    .comment("The amount of ticks between each action")
                    .defineInRange("timing.ticksBetween", 10, 1, Integer.MAX_VALUE);
            builder.pop();

            // TODO: [config] White/blacklist support for mods
            // TODO: [config] White/blacklist support for block groups
            // TODO: [config] White/blacklist support for forge groups (crops for example)
            // TODO: [config/actions] Configurable delays for each mod / group / action
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

}
