package dev.tmp.StareTillTheyGrow.Network;

import dev.tmp.StareTillTheyGrow.Network.Messages.RegisterBlockNetworkMessage;
import dev.tmp.StareTillTheyGrow.Network.Messages.RegisterEntityNetworkMessage;
import dev.tmp.StareTillTheyGrow.Network.Messages.UnregisterNetworkMessage;
import dev.tmp.StareTillTheyGrow.StareTillTheyGrow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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

    public static void initialize() {
        int id = 0;
        INSTANCE.registerMessage(id++, RegisterBlockNetworkMessage.class, RegisterBlockNetworkMessage::encode, RegisterBlockNetworkMessage::decode, RegisterBlockNetworkMessage::handle);
        INSTANCE.registerMessage(id++, RegisterEntityNetworkMessage.class, RegisterEntityNetworkMessage::encode, RegisterEntityNetworkMessage::decode, RegisterEntityNetworkMessage::handle);
        INSTANCE.registerMessage(id++, UnregisterNetworkMessage.class, UnregisterNetworkMessage::encode, UnregisterNetworkMessage::decode, UnregisterNetworkMessage::handle);
    }

    public static void toPlayer(ServerPlayer player, Object message) {
        INSTANCE.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void toPlayers(Object message) {
        // TODO: Make work when needed.
    }

    public static void toServer(Object message) {
        INSTANCE.sendToServer(message);
    }
}
