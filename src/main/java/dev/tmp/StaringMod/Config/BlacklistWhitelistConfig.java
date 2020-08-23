package dev.tmp.StaringMod.Config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
                .comment("Use either the blackList of whiteList")
                .define("blackListWhiteList.useBlackList", true);

        blackList = server
                .comment("The blackList")
                .defineList("blackListWhiteList.blackList", defaultBlackList, entry -> entry instanceof String);

        whiteList = server
                .comment("The whiteList")
                .defineList("blackListWhiteList.whiteList", defaultWhiteList, entry -> entry instanceof String);
    }

}
