package dev.tmp.StareTillTheyGrow.Network.Messages;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UnregisterNetworkMessage extends AbstractNetworkMessage {

    public UnregisterNetworkMessage() {}

    public static void encode(UnregisterNetworkMessage message, FriendlyByteBuf buffer) {}

    public static UnregisterNetworkMessage decode(FriendlyByteBuf buffer) {
        return new UnregisterNetworkMessage();
    }

    public static void handle(final UnregisterNetworkMessage message, final Supplier<NetworkEvent.Context> supplierContext) {
        handleMessage(message, supplierContext);
    }

    static void handlePlayerMessage(final Object message, Player player){
        PlayerTargetDictionary.unregister(player);
    };
}
