package dev.tmp.StaringMod.EventHandlers;

import dev.tmp.StaringMod.Library.GrowableBlockDictionary;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerTickEventHandlers {

    int counter = 0;

    @SubscribeEvent
    public void lookedAtGrowableEvent ( TickEvent.ServerTickEvent event ) {
        if ( event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END ) {
            counter++;

            if ( counter >= 40 ) {
                counter = 0;

                // Apply grow to blocks
                GrowableBlockDictionary.forEach((__, worldBlockPos) -> {
                    ServerWorld world = worldBlockPos.getWorld();
                    BlockPos blockPos = worldBlockPos.getBlockPos();
                    BlockState blockState = world.getBlockState( blockPos );
                    Block block = blockState.getBlock();
                    if ( block instanceof IGrowable ) {
                        IGrowable growable = (IGrowable) block;
                        world.spawnParticle(
                            ParticleTypes.HAPPY_VILLAGER,
                            blockPos.getX(),
                            blockPos.getY(),
                            blockPos.getZ(),
                            75,
                            1,
                            1,
                            1,
                            1
                        );
                        growable.grow( world, world.rand, blockPos, blockState);
                    }
                });
            }
        }
    }

}
