package dev.tmp.StareTillTheyGrow.Network.Messages;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

public class UnregisterNetworkMessage {

    public UnregisterNetworkMessage() {}

    public static void encode(UnregisterNetworkMessage message, FriendlyByteBuf buffer) {}

    public static UnregisterNetworkMessage decode(FriendlyByteBuf buffer) {
        return new UnregisterNetworkMessage();
    }

    public static void handle(final UnregisterNetworkMessage message, final Supplier<NetworkEvent.Context> supplierContext) {
        NetworkEvent.Context context = supplierContext.get();

        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (null != player) {
                    PlayerTargetDictionary.unregister(player);
                }
            });
        }

        context.setPacketHandled(true);
    }
}
