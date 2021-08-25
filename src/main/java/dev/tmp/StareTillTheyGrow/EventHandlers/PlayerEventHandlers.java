package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Library.ActionType;
import dev.tmp.StareTillTheyGrow.Library.ActionTypeHelper;
import dev.tmp.StareTillTheyGrow.Network.Message.RegisterBlock;
import dev.tmp.StareTillTheyGrow.Network.Message.RegisterEntity;
import dev.tmp.StareTillTheyGrow.Network.Message.Unregister;
import dev.tmp.StareTillTheyGrow.Network.Network;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.FOVUpdateEvent;
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
            RayTraceResult hit = getRayTraceResult();
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

    private RayTraceResult getRayTraceResult() {
        Minecraft minecraftInstance = Minecraft.getInstance();
        return minecraftInstance.hitResult;
    }

    private boolean handleHitEvent(RayTraceResult hit) {
        if(hit.getType() == RayTraceResult.Type.BLOCK) {
            return handleBlockHitEvent((BlockRayTraceResult) hit);
        } else if(hit.getType() == RayTraceResult.Type.ENTITY) {
            return handleEntityHitEvent((EntityRayTraceResult) hit);
        }

        return false;
    }

    private boolean handleBlockHitEvent(BlockRayTraceResult hit) {
        Minecraft minecraftInstance = Minecraft.getInstance();
        ClientWorld world = minecraftInstance.level;
        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if(block instanceof IGrowable) {
            IGrowable targetBlock =(IGrowable) block;
            if(targetBlock.isValidBonemealTarget(world, pos, state, world.isClientSide)) {
                registerBonemealableBlock(pos);
                return true;
            }
        }

        return false;
    }


    private boolean handleEntityHitEvent(EntityRayTraceResult hit) {
        Entity entity = hit.getEntity();
        UUID entityUuid = entity.getUUID();

        if(entity instanceof AnimalEntity) {
            AnimalEntity animal = (AnimalEntity) entity;
            if(animal.isBaby()) {
                registerEntity(ActionType.GROW_UP, entityUuid);
                return true;
            } else if(animal instanceof SheepEntity) {
                SheepEntity sheep = (SheepEntity) animal;
                if(sheep.isSheared()) {
                    registerEntity(ActionType.REGROW_WOOL, entityUuid);
                    return true;
                }
            }
        }

        return false;
    }

    private void registerBonemealableBlock(BlockPos pos) {
        registerBlock(ActionType.BONE_MEAL_BLOCK, pos);
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
