package dev.tmp.StareTillTheyGrow.Network.Messages;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RegisterBlockNetworkMessage extends AbstractNetworkMessage {

    private final double x;
    private final double y;
    private final double z;

    // Initialize with given x,y,z positions
    public RegisterBlockNetworkMessage(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Initialize with BlockPos
    public RegisterBlockNetworkMessage(BlockPos blockPos) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }

    public BlockPos getBlockPost() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public static void encode(RegisterBlockNetworkMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    public static RegisterBlockNetworkMessage decode(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new RegisterBlockNetworkMessage(x, y, z);
    }

    public static void handle(final RegisterBlockNetworkMessage message, final Supplier<NetworkEvent.Context> supplierContext) {
        NetworkEvent.Context context = supplierContext.get();

        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (null != player) {
                    PlayerTargetDictionary.registerBlock(
                            player,
                            player.getLevel(),
                            message.getBlockPost()
                    );
                }
            });
        }

        supplierContext.get().setPacketHandled(true);
    }
}
