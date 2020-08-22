package dev.tmp.StaringMod.Network;

import dev.tmp.StaringMod.Network.Message.RegisterBlock;
import dev.tmp.StaringMod.Network.Message.UnregisterBlock;
import dev.tmp.StaringMod.StaringMod;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network {

    // Protocol Version Constant
    private static final String PROTOCOL_VERSION = "1";

    // Create Network Instance
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            // Create research for the Channel
            new ResourceLocation(StaringMod.MODE_ID, "main"),
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
    public static void sendTo(ServerPlayerEntity player, Object msg) {
        INSTANCE.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    // Sends a message from a player to the server
    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

}
