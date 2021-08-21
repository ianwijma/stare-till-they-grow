package dev.tmp.StareTillTheyGrow.Network.Message;

import dev.tmp.StareTillTheyGrow.Library.PlayerTargetDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class Unregister {

    public Unregister() {}

    // The encoding before sending
    public static void encode(Unregister msg, FriendlyByteBuf buf ) {}

    // The decoding after receiving.
    public static Unregister decode(FriendlyByteBuf buf) {
        return new Unregister();
    }

    // Handing the received data
    public static void handle(final Unregister registerBlock, final Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        // Check if this is server sided
        if ( context.getDirection().getReceptionSide().isServer() ) {
            // Enqueue the task
            context.enqueueWork(() -> {
                // Get the player who send the command
                ServerPlayer player = ctx.get().getSender();
                if ( null != player ) {
                    PlayerTargetDictionary.unregister( player );
                }
            });
        }
        context.setPacketHandled(true);
    }

}
