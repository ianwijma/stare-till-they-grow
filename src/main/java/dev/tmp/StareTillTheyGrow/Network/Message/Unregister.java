package dev.tmp.StareTillTheyGrow.Network.Message;

import dev.tmp.StareTillTheyGrow.Library.PlayerTargetDictionary;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;


import java.util.function.Supplier;

public class Unregister {

    public Unregister() {}

    // The encoding before sending
    public static void encode(Unregister msg, PacketBuffer buf ) {}

    // The decoding after receiving.
    public static Unregister decode(PacketBuffer buf) {
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
                ServerPlayerEntity player = ctx.get().getSender();
                if ( null != player ) {
                    PlayerTargetDictionary.unregister( player );
                }
            });
        }
        context.setPacketHandled(true);
    }

}
