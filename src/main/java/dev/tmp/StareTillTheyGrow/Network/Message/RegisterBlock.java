package dev.tmp.StareTillTheyGrow.Network.Message;

import dev.tmp.StareTillTheyGrow.Library.GrowableBlockDictionary;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RegisterBlock {

    private final double x;
    private final double y;
    private final double z;

    // Initialize with given x,y,z positions
    public RegisterBlock(double x, double y, double z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Initialize with BlockPos
    public RegisterBlock( BlockPos blockPos ) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }

    public BlockPos getBlockPos () {
        return new BlockPos( this.x, this.y, this.z );
    }

    // The encoding before sending
    public static void encode( RegisterBlock msg, PacketBuffer buf ) {
        buf.writeDouble( msg.x );
        buf.writeDouble( msg.y );
        buf.writeDouble( msg.z );
    }

    // The decoding after receiving.
    public static RegisterBlock decode(PacketBuffer buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new RegisterBlock( x, y, z );
    }

    // Handing the received data
    public static void handle(final RegisterBlock registerBlock, final Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        // Check if this is server sided
        if ( context.getDirection().getReceptionSide().isServer() ) {
            // Enqueue the task
            context.enqueueWork(() -> {
                // Get the player who send the command
                ServerPlayerEntity player = ctx.get().getSender();
                if ( null != player ) {
                    ServerWorld world = player.getServerWorld();
                    BlockPos blockPos = registerBlock.getBlockPos();
                    GrowableBlockDictionary.register( world, blockPos );
                }
            });
        }
        context.setPacketHandled(true);
    }

}
