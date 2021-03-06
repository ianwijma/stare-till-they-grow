package dev.tmp.StareTillTheyGrow.EventHandlers;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Network.Message.RegisterBlock;
import dev.tmp.StareTillTheyGrow.Network.Message.UnregisterBlock;
import dev.tmp.StareTillTheyGrow.Network.Network;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;


public class PlayerEventHandlers {

    @Nullable
    private BlockPos previousBlockPos = null;

    @SubscribeEvent
    public void lookedAtGrowableEvent ( FOVUpdateEvent event ) {
        // Get the target
        Minecraft minecraftInstance = Minecraft.getInstance();
        RayTraceResult lookingAt = minecraftInstance.hitResult;

        boolean shiftToActivate = Config.COMMON.shiftToActivate.get();
        boolean playerActivation = !shiftToActivate || minecraftInstance.player.isShiftKeyDown();

        if ( !playerActivation || null == lookingAt ) {
            unregister();
        } else if ( playerActivation && lookingAt.getType() == RayTraceResult.Type.BLOCK ) {
            // Get the blockPos
            BlockPos blockPos = ((BlockRayTraceResult) lookingAt).getBlockPos();

            boolean growable = isGrowable( blockPos, event );

            if ( null == previousBlockPos && growable ) {
                // Check if the th
                register( blockPos );
            } else if ( blockPosChanged( blockPos ) ) {
                if ( growable ) {
                    register( blockPos );
                } else {
                    unregister();
                }
            }
        } else {
            unregister();
        }
    }

    private void unregister () {
        if ( null != previousBlockPos ) {
            // Unregister
            Network.sendToServer(new UnregisterBlock( previousBlockPos ));
            previousBlockPos = null;
        }
    }

    private void register ( BlockPos blockPos ) {
        // Unregister previous blockPos
        unregister();

        // Register the new growable block
        Network.sendToServer(new RegisterBlock( blockPos ));
        previousBlockPos = blockPos;
    }

    private boolean isGrowable ( BlockPos blockPos, FOVUpdateEvent event ) {
        // Get the block state
        World world = event.getEntity().level;
        BlockState blockState = world.getBlockState( blockPos );
        // Check if the block is Growable
        if ( blockState.getBlock() instanceof IGrowable ) {
            IGrowable iGrowable = (IGrowable) blockState.getBlock();
            if ( iGrowable.isValidBonemealTarget(world, blockPos, blockState, world.isClientSide) ) {
                return true;
            }
        }

        return false;
    }

    private boolean blockPosChanged ( BlockPos current ) {
        // Check if there was a previous state
        if ( null == previousBlockPos ) return true;

        // Else return stuff
        return  previousBlockPos.getX() != current.getX() ||
                previousBlockPos.getY() != current.getY() ||
                previousBlockPos.getZ() != current.getZ();
    }

}
