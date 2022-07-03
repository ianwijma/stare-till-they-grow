package dev.tmp.StareTillTheyGrow.Network.Messages;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RegisterEntityNetworkMessage {
    private final UUID entityUuid;

    public RegisterEntityNetworkMessage(Entity entity) {
        this.entityUuid = entity.getUUID();
    }

    public RegisterEntityNetworkMessage(UUID entityUuid) {
        this.entityUuid = entityUuid;
    }

    public static void encode(RegisterEntityNetworkMessage message, FriendlyByteBuf buffer) {
        buffer.writeUUID(message.entityUuid);
    }

    public static RegisterEntityNetworkMessage decode(FriendlyByteBuf buffer) {
        UUID uuid = buffer.readUUID();
        return new RegisterEntityNetworkMessage(uuid);
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public static void handle(final RegisterEntityNetworkMessage message, final Supplier<NetworkEvent.Context> supplierContext) {
        NetworkEvent.Context context = supplierContext.get();

        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (null != player) {
                    PlayerTargetDictionary.registerEntity(
                            player,
                            player.getLevel(),
                            message.getEntityUuid()
                    );
                }
            });
        }

        context.setPacketHandled(true);
    }
}
