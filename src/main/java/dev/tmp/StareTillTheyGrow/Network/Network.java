package dev.tmp.StareTillTheyGrow.Network;

import dev.tmp.StareTillTheyGrow.Network.Message.RegisterBlock;
import dev.tmp.StareTillTheyGrow.Network.Message.UnregisterBlock;
import dev.tmp.StareTillTheyGrow.StareTillTheyGrow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class Network {

    // Protocol Version Constant
    private static final String PROTOCOL_VERSION = "1";

    // Create Network Instance
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            // Create research for the Channel
            new ResourceLocation(StareTillTheyGrow.MOD_ID, "main"),
            // Return protocol version
            () -> PROTOCOL_VERSION,
            // Client protocol version check
            PROTOCOL_VERSION::equals,
            // Server protocol version check
            PROTOCOL_VERSION::equals
    );

    // Registers all messages
    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, RegisterBlock.class, RegisterBlock::encode, RegisterBlock::decode, RegisterBlock::handle);
        INSTANCE.registerMessage(id++, UnregisterBlock.class, UnregisterBlock::encode, UnregisterBlock::decode, UnregisterBlock::handle);
    }

    // Sends a message from the server to a player
    public static void sendTo(ServerPlayer player, Object msg) {
        INSTANCE.sendTo(msg, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    // Sends a message from a player to the server
    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

}
