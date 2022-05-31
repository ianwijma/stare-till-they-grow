package dev.tmp.StareTillTheyGrow.Client;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Library.ActionType;
import dev.tmp.StareTillTheyGrow.Network.Message.RegisterBlock;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class PlayerEventHandlers {

    private final Minecraft minecraft = Minecraft.getInstance();
    private @Nullable
    BlockPos previousBlockPos = null;
    private @Nullable
    ActionType previousActionType = null;
    private @Nullable
    UUID previousEntityUuid = null;

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
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

    private boolean handleBlockHit(Level level, BlockHitResult blockHitResult) {
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

    private boolean handleEntityHit(Level level, EntityHitResult entityHitResult) {
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

    private void registerBlock(ActionType actionType, BlockPos pos) {
        if (actionType.enabled() && ((actionTypeChanged(actionType) || blockPosChanged(pos)))) {
            Network.sendToServer(new RegisterBlock(actionType, pos));
            previousActionType = actionType;
            previousBlockPos = pos;
        }
    }

    private void registerEntity(ActionType actionType, UUID uuid) {

    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    private void unregisterAll() {
        Network.sendToServer(new Unregister());
        previousActionType = null;
        previousBlockPos = null;
        previousEntityUuid = null;
    }

    // Methods retained for minimal file changes; will be condensed later
    // Null checks & proper comparison all covered in #equals

    private boolean blockPosChanged(BlockPos currentBlockPos) {
        return !currentBlockPos.equals(previousBlockPos);
    }

    private boolean entityUuidChanged(UUID currentEntityUuid) {
        return !currentEntityUuid.equals(previousEntityUuid);
    }

    private boolean actionTypeChanged(ActionType currentActionType) {
        return !currentActionType.equals(previousActionType);
    }

}
