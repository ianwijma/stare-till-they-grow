package dev.tmp.StareTillTheyGrow.Config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

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
        // The growth delay
        public static ForgeConfigSpec.IntValue delay;
        // The apply rate
        public static ForgeConfigSpec.IntValue everyXSeconds;
        // Either use the blacklist of whitelist
        public static ForgeConfigSpec.BooleanValue shiftToActivate;
        // Either use the blacklist of whitelist
        public static ForgeConfigSpec.BooleanValue useBlackList;
        // The blacklist
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> blackList;
        private final static ArrayList<String> defaultBlackList = Lists.newArrayList( "minecraft:grass" );
        // The whitelist
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteList;
        private final static ArrayList<String> defaultWhiteList = Lists.newArrayList();

        public Common ( ForgeConfigSpec.Builder builder ) {
            builder.comment("Delay and apply rate").push("timings");
            delay = builder
                    .comment("The delay in seconds before we start applying bonemeal")
                    .defineInRange("timing.delay", 2, 1, 60);

            everyXSeconds = builder
                    .comment("The time in seconds between each applying of bonemeal")
                    .defineInRange("timing.everyXSeconds", 1, 1, 60);
            builder.pop();

            builder.comment("General settings").push("general");
            shiftToActivate = builder
                    .comment("If you need to bend over (hold shift) to activate your staring powers")
                    .define("general.shiftToActivate", false);
            builder.pop();


            builder.comment("blackList or whiteList settings").push("blacklistwhitelist");
            useBlackList = builder
                    .comment("You can choose to use either a blackList or a whiteList")
                    .define("blackListWhiteList.useBlackList", true);

            blackList = builder
                    .comment("Disallow items for being stared at")
                    .defineList("blackListWhiteList.blackList", defaultBlackList, entry -> entry instanceof String);

            whiteList = builder
                    .comment("Only allow certain items to be stared at")
                    .defineList("blackListWhiteList.whiteList", defaultWhiteList, entry -> entry instanceof String);
            builder.pop();

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
