package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Network.Messages.RegisterBlockNetworkMessage;
import dev.tmp.StareTillTheyGrow.Network.Messages.RegisterEntityNetworkMessage;
import dev.tmp.StareTillTheyGrow.Network.Messages.UnregisterNetworkMessage;
import dev.tmp.StareTillTheyGrow.Network.Network;
import dev.tmp.StareTillTheyGrow.StareTillTheyGrow;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = StareTillTheyGrow.MOD_ID, value = Dist.CLIENT)
public final class ClientTickEventHandler {
    private static @Nullable BlockPos currentBlockPos = null;
    private static @Nullable UUID currentEntityUuid = null;

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        // Only run when in a world or connected to a server
        if (null == minecraft.level) return;

        if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
            registerBlockHitResult(blockHitResult);
        } else if (minecraft.hitResult instanceof EntityHitResult entityHitResult) {
            registerEntityHitResult(entityHitResult);
        } else {
            unregisterHitResults();
        }
    }

    private static void registerBlockHitResult(BlockHitResult blockHitResult) {
        BlockPos newBlockPos = blockHitResult.getBlockPos();
        if (null == currentBlockPos || !currentBlockPos.equals(newBlockPos)) {
            currentBlockPos = newBlockPos;
            currentEntityUuid = null;
            Network.sendToServer(new RegisterBlockNetworkMessage(newBlockPos));
        }
    }

    private static void registerEntityHitResult(EntityHitResult entityHitResult) {
        UUID newEntityUuid = entityHitResult.getEntity().getUUID();
        if (null == currentEntityUuid || !currentEntityUuid.equals(newEntityUuid)) {
            currentEntityUuid = newEntityUuid;
            currentBlockPos = null;
            Network.sendToServer(new RegisterEntityNetworkMessage(newEntityUuid));
        }
    }

    private static void unregisterHitResults() {
        currentBlockPos = null;
        currentEntityUuid = null;
        Network.sendToServer(new UnregisterNetworkMessage());
    }
}