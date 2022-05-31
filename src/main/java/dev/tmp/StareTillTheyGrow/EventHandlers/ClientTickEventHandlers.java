package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Library.ActionType;
import dev.tmp.StareTillTheyGrow.Network.Message.RegisterBlock;
import dev.tmp.StareTillTheyGrow.Network.Message.RegisterEntity;
import dev.tmp.StareTillTheyGrow.Network.Message.Unregister;
import dev.tmp.StareTillTheyGrow.Network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.UUID;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(Dist.CLIENT)
public final class ClientTickEventHandlers {

    private static @Nullable
    BlockPos previousBlockPos = null;
    private static @Nullable
    ActionType previousActionType = null;
    private static @Nullable
    UUID previousEntityUuid = null;

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        // Only run when in a world or connected to a server
        if (minecraft.level == null) return;

        if (!Config.COMMON.shiftToActivate.get() || Objects.requireNonNull(minecraft.player).isShiftKeyDown()) {
            Level level = minecraft.level;

            // Block hit
            if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
                // If the targeted block is invalid
                if (!handleBlockHit(level, blockHitResult)) {
                    unregisterAll();
                }
            } else if (minecraft.hitResult instanceof EntityHitResult entityHitResult) {
                // If the targeted entity is invalid
                if (!handleEntityHit(level, entityHitResult)) {
                    unregisterAll();
                }
            }
        }
    }

    private static boolean handleBlockHit(Level level, BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.getBlock() instanceof BonemealableBlock block) {
            // If the block can be bonemealed
            if (block.isValidBonemealTarget(level, blockPos, blockState, level.isClientSide())) {
                registerBlock(ActionType.BONE_MEAL_BLOCK, blockPos);
                return true;
            }
        } else if (blockState.getBlock() instanceof CakeBlock) {
            // Avoid needless packet if the cake is full
            if (blockState.getValue(CakeBlock.BITES) == 0) {
                registerBlock(ActionType.CAKE_REGROWTH, blockPos);
                return true;
            }
        }

        return false;
    }

    private static boolean handleEntityHit(Level level, EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        UUID entityUuid = entity.getUUID();

        if (entity instanceof Animal animal) {
            if (animal.isBaby()) {
                registerEntity(ActionType.GROW_UP, entityUuid);
                return true;
            } else if (animal instanceof Sheep sheep && sheep.isSheared()) {
                registerEntity(ActionType.REGROW_WOOL, entityUuid);
                return true;
            } else if (animal.canFallInLove()) {
                registerEntity(ActionType.FALL_IN_LOVE, entityUuid);
                return true;
            }
        }

        return false;
    }

    private static void registerBlock(ActionType actionType, BlockPos pos) {
        if (actionType.enabled() && ((actionTypeChanged(actionType) || blockPosChanged(pos)))) {
            Network.sendToServer(new RegisterBlock(actionType, pos));
            previousActionType = actionType;
            previousBlockPos = pos;
        }
    }

    private static void registerEntity(ActionType actionType, UUID uuid) {
        if (actionType.enabled() && ((actionTypeChanged(actionType) || entityUuidChanged(uuid)))) {
            Network.sendToServer(new RegisterEntity(actionType, uuid));
            previousActionType = actionType;
            previousEntityUuid = uuid;
        }
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    private static void unregisterAll() {
        Network.sendToServer(new Unregister());
        previousActionType = null;
        previousBlockPos = null;
        previousEntityUuid = null;
    }

    // Methods retained for minimal file changes; will be condensed later
    // Null checks & proper comparison all covered in #equals

    private static boolean blockPosChanged(BlockPos currentBlockPos) {
        return !currentBlockPos.equals(previousBlockPos);
    }

    private static boolean entityUuidChanged(UUID currentEntityUuid) {
        return !currentEntityUuid.equals(previousEntityUuid);
    }

    private static boolean actionTypeChanged(ActionType currentActionType) {
        return !currentActionType.equals(previousActionType);
    }

}
