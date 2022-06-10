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
        // Defaults
        private static final ArrayList<String> defaultBlackList = Lists.newArrayList( "minecraft:grass_block", "minecraft:grass", "minecraft:tall_grass" );
        private static final ArrayList<String> defaultWhiteList = Lists.newArrayList();

        // The growth delay
        public ForgeConfigSpec.IntValue delay;
        // The apply rate
        public ForgeConfigSpec.IntValue everyXSeconds;
        // Either use the blacklist of whitelist
        public ForgeConfigSpec.BooleanValue shiftToActivate;
        // Enable/disable bone meal effect
        public ForgeConfigSpec.BooleanValue applyBoneMeal;
        // Enable/disable growing of babies
        public ForgeConfigSpec.BooleanValue growBabies;
        // Enable/disable regrowing of wool
        public ForgeConfigSpec.BooleanValue regrowWool;
        // Enable/disable making animals fall in love
        public ForgeConfigSpec.BooleanValue fallInLove;
        // Enable/disable Making cake regrow pieces
        public ForgeConfigSpec.BooleanValue cakeRegrowth;
        // Either use the blacklist of whitelist
        public ForgeConfigSpec.BooleanValue useBlackList;
        // The blacklist
        public ForgeConfigSpec.ConfigValue<List<? extends String>> blackList;
        // The whitelist
        public ForgeConfigSpec.ConfigValue<List<? extends String>> whiteList;

        public Common ( ForgeConfigSpec.Builder builder ) {
            builder.comment("Delay and apply rate").push("timings");
            delay = builder
                    .comment("The delay in seconds before we start growing")
                    .defineInRange("timing.delay", 2, 1, 60);

            everyXSeconds = builder
                    .comment("The time in seconds between each grow application")
                    .defineInRange("timing.everyXSeconds", 1, 1, 60);
            builder.pop();

            builder.comment("General settings").push("general");
            shiftToActivate = builder
                    .comment("If you need to hold shift (bend over) to activate your staring powers")
                    .define("general.shiftToActivate", false);
            applyBoneMeal = builder
                    .comment("If staring at plants applies bone meal")
                    .define("general.applyBoneMeal", true);
            growBabies = builder
                    .comment("If staring at baby animals makes them grow")
                    .define("general.growBabies", true);
            regrowWool = builder
                    .comment("If staring at sheared sheep makes their wool grow back")
                    .define("general.regrowWool", true);
            fallInLove = builder
                    .comment("If staring at a animal makes them fall in love")
                    .define("general.fallInLove", true);
            cakeRegrowth = builder
                    .comment("If staring at cake makes it regrow pieces")
                    .define("general.cakeRegrowth", true);
            builder.pop();


            builder.comment("blackList or whiteList settings").push("blacklistwhitelist");
            useBlackList = builder
                    .comment("You can choose to use either a blackList or a whiteList")
                    .define("blackListWhiteList.useBlackList", true);

            blackList = builder
                    .comment("Disallow blocks to be stared at")
                    .defineList("blackListWhiteList.blackList", defaultBlackList, entry -> entry instanceof String);

            whiteList = builder
                    .comment("Only allow certain blocks to be stared at")
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
