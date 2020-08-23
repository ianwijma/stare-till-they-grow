package dev.tmp.StareTillTheyGrow.Config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class BlacklistWhitelistConfig {

    public static ForgeConfigSpec.BooleanValue useBlackList;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> blackList;
    private final static ArrayList<String> defaultBlackList = Lists.newArrayList( "minecraft:grass" );

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteList;
    private final static ArrayList<String> defaultWhiteList = Lists.newArrayList();

    public static void init ( ForgeConfigSpec.Builder server ) {
        server.comment("blackList or whiteList settings");

        useBlackList = server
                .comment("You can choose to use either a blackList or a whiteList")
                .define("blackListWhiteList.useBlackList", true);

        blackList = server
                .comment("Disallow items for being stared at")
                .defineList("blackListWhiteList.blackList", defaultBlackList, entry -> entry instanceof String);

        whiteList = server
                .comment("Only allow certain items to be stared at")
                .defineList("blackListWhiteList.whiteList", defaultWhiteList, entry -> entry instanceof String);
    }

}
