package dev.tmp.StareTillTheyGrow.Network.Message;

import dev.tmp.StareTillTheyGrow.Library.ActionType;
import dev.tmp.StareTillTheyGrow.Library.PlayerTargetDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RegisterEntity {

    private final ActionType actionType;
    private final UUID entityUuid;

    // Initialize with given x,y,z positions
    public RegisterEntity(ActionType actionType, UUID entityUuid) {
        this.actionType = actionType;
        this.entityUuid = entityUuid;
    }

    public UUID getEntityUuid() {
        return this.entityUuid;
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    // The encoding before sending
    public static void encode(RegisterEntity msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.actionType);
        buf.writeUUID(msg.entityUuid);
    }

    // The decoding after receiving.
    public static RegisterEntity decode(FriendlyByteBuf buf) {
        ActionType actionType = buf.readEnum(ActionType.class);
        UUID entityUuid = buf.readUUID();
        return new RegisterEntity(actionType, entityUuid);
    }

    // Handing the received data
    public static void handle(final RegisterEntity registerBlock, final Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        // Check if this is server sided
        if (context.getDirection().getReceptionSide().isServer()) {
            // Enqueue the task
            context.enqueueWork(() -> {
                // Get the player who send the command
                ServerPlayer player = ctx.get().getSender();
                if (null != player) {
                    ServerLevel world = player.server.overworld();
                    UUID entityUuid = registerBlock.getEntityUuid();
                    Entity entity = world.getEntity(entityUuid);
                    ActionType actionType = registerBlock.getActionType();
                    PlayerTargetDictionary.registerEntity(actionType, world, player, entity);
                }
            });
        }
        context.setPacketHandled(true);
    }

}
