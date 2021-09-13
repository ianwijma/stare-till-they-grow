package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Library.ActionType;
import dev.tmp.StareTillTheyGrow.Library.ActionTypeHelper;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.UUID;


public class PlayerEventHandlers {

    @Nullable
    private ActionType previousActionType = null;

    @Nullable
    private BlockPos previousBlockPos = null;

    @Nullable
    private UUID previousEntityUuid = null;


    @SubscribeEvent
    public void lookAroundEvent(FOVUpdateEvent event) {
        if(shouldHandleEvent()) {
            HitResult hit = getHitResult();
            if(!handleHitEvent(hit)) {
                unregisterAll();
            }
        } else {
            unregisterAll();
        }
    }

    private boolean shouldHandleEvent() {
        Minecraft minecraftInstance = Minecraft.getInstance();
        boolean shiftToActivate = Config.COMMON.shiftToActivate.get();
        return !shiftToActivate || minecraftInstance.player.isShiftKeyDown();
    }

    private HitResult getHitResult() {
        Minecraft minecraftInstance = Minecraft.getInstance();
        return minecraftInstance.hitResult;
    }

    private boolean handleHitEvent(HitResult hit) {
        if(hit.getType() == HitResult.Type.BLOCK) {
            return handleBlockHitEvent((BlockHitResult) hit);
        } else if(hit.getType() == HitResult.Type.ENTITY) {
            return handleEntityHitEvent((EntityHitResult) hit);
        }

        return false;
    }

    private boolean handleBlockHitEvent(BlockHitResult hit) {
        Minecraft minecraftInstance = Minecraft.getInstance();
        Level world = minecraftInstance.level;
        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if(block instanceof BonemealableBlock) {
            BonemealableBlock targetBlock =(BonemealableBlock) block;
            if(targetBlock.isValidBonemealTarget(world, pos, state, world.isClientSide)) {
                registerBonemealableBlock(pos);
                return true;
            }
        } else if (block instanceof CakeBlock) {
            registerCakeBlock(pos);
            return true;
        }

        return false;
    }


    private boolean handleEntityHitEvent(EntityHitResult hit) {
        Entity entity = hit.getEntity();
        UUID entityUuid = entity.getUUID();

        if(entity instanceof Animal) {
            Animal animal = (Animal) entity;
            if(animal.isBaby()) {
                registerEntity(ActionType.GROW_UP, entityUuid);
                return true;
            } else {
                if(animal instanceof Sheep && ((Sheep) animal).isSheared()) {
                    registerEntity(ActionType.REGROW_WOOL, entityUuid);
                    return true;
                } else if(animal.canFallInLove()) {
                    registerEntity(ActionType.FALL_IN_LOVE, entityUuid);
                    return true;
                }
            }
        }

        return false;
    }

    private void registerBonemealableBlock(BlockPos pos) {
        registerBlock(ActionType.BONE_MEAL_BLOCK, pos);
    }

    private void registerCakeBlock(BlockPos pos) {
        registerBlock(ActionType.CAKE_REGROWTH, pos);
    }

    private void registerBlock(ActionType actionType, BlockPos pos) {
        // Register the new block if needed
        if(ActionTypeHelper.actionTypeEnabled(actionType)) {
            if(actionTypeChanges(actionType) || blockPosChanged(pos)) {
                Network.sendToServer(new RegisterBlock( actionType, pos ));
                previousActionType = actionType;
                previousBlockPos = pos;
            }
        }
    }

    private void registerEntity(ActionType actionType, UUID uuid) {
        // Register the new entity
        if(ActionTypeHelper.actionTypeEnabled(actionType)) {
            if(actionTypeChanges(actionType) || entityUuidChanged(uuid)) {
                Network.sendToServer(new RegisterEntity( actionType, uuid ));
                previousActionType = actionType;
                previousEntityUuid = uuid;
            }
        }
    }

    private void unregisterAll() {
        Network.sendToServer(new Unregister());
        previousActionType = null;
        previousBlockPos = null;
        previousEntityUuid = null;

    }

    private boolean blockPosChanged(BlockPos currentBlockPos) {
        // Check if there was a previous state
        if ( null == previousBlockPos ) return true;

        // Else return stuff
        return  previousBlockPos.getX() != currentBlockPos.getX() ||
                previousBlockPos.getY() != currentBlockPos.getY() ||
                previousBlockPos.getZ() != currentBlockPos.getZ();
    }

    private boolean entityUuidChanged(UUID currentEntityUuid) {
        // Check if there was a previous state
        if ( null == previousEntityUuid ) return true;

        // Else return stuff
        return  previousEntityUuid != currentEntityUuid;
    }

    private boolean actionTypeChanges(ActionType currentActionType) {
        // Check if there was a previous state
        if ( null == previousActionType ) return true;

        // Else return stuff
        return  previousActionType != currentActionType;
    }

}
