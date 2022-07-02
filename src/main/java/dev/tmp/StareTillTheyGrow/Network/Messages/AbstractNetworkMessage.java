package dev.tmp.StareTillTheyGrow.Network.Messages;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class AbstractNetworkMessage {

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

        supplierContext.get().setPacketHandled(true);
    }

    protected static void handleMessage(final Object message, final Supplier<NetworkEvent.Context> supplierContext) {
        NetworkEvent.Context context = supplierContext.get();

        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (null != player) {
                    handlePlayerMessage(message, player);
                }
            });
        }

        supplierContext.get().setPacketHandled(true);
    }

    static void handlePlayerMessage(final Object message, Player player){};
}
