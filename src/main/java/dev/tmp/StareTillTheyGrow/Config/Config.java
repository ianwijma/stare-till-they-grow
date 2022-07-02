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


        public Common ( ForgeConfigSpec.Builder builder ) {
            // Build the common config
            // TODO: [config] More organised config
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
