package dev.tmp.StareTillTheyGrow.Library;

import dev.tmp.StareTillTheyGrow.Config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.BiConsumer;

public class GrowableBlockDictionary {

    // The delay before applying growth
    static long delay = Config.COMMON.delay.get();
    // Applies growth every x seconds
    static long everyXSeconds = Config.COMMON.everyXSeconds.get();

    // To either use the blackList or whiteList
    static boolean useBlackList = Config.COMMON.useBlackList.get();
    // The blackList
    static String[] blackList = Arrays.copyOf( Config.COMMON.blackList.get().toArray(), Config.COMMON.blackList.get().size(), String[].class );
    // The whiteList
    static String[] whiteList = Arrays.copyOf( Config.COMMON.whiteList.get().toArray(), Config.COMMON.whiteList.get().size(), String[].class );

    private static final Hashtable<String, WorldBlockPos> HASHTABLE = new Hashtable<>();

    public static boolean register ( ServerWorld world, BlockPos blockPos ) {
        return register( new WorldBlockPos( world, blockPos ) );
    }
    public static boolean register ( WorldBlockPos worldBlockPos ) {
        Block block = worldBlockPos.getBlock();
        if ( canRegister( block ) ) {
            String key = worldBlockPos.getKey();

            if ( !HASHTABLE.containsKey( key ) ) {
                HASHTABLE.put( key, worldBlockPos );
                return true;
            }
        }

        return false;
    }

    private static boolean canRegister (Block block ) {
        String name = block.getRegistryName().toString();

        if (useBlackList) {
            return !ArrayUtils.contains( blackList, name );
        }

        return ArrayUtils.contains( whiteList, name );
    }

    public static boolean unregister ( ServerWorld world, BlockPos blockPos ) {
        return unregister( new WorldBlockPos( world, blockPos ) );
    }
    public static boolean unregister ( WorldBlockPos worldBlockPos ) {
        String key = worldBlockPos.getKey();

        if ( HASHTABLE.containsKey( key ) ) {
            HASHTABLE.remove( key );
            return true;
        }

        return false;
    }

    public static void forEach (BiConsumer<String, WorldBlockPos> worldBlockPosConsumer ) {
        HASHTABLE.forEach( worldBlockPosConsumer );
    }

    public static class WorldBlockPos {

        private final ServerWorld world;
        private final BlockPos blockPos;
        private final Date createdDate;
        private long lastSecondGrown;

        public WorldBlockPos ( ServerWorld world, BlockPos blockPos ) {
            this.world = world;
            this.blockPos = blockPos;
            this.createdDate = new Date();
        }

        private long getSecondsSinceRegistration() {
            long created = this.createdDate.getTime();
            long now = (new Date()).getTime();
            return (now-created)/1000;
        }

        private ServerWorld getWorld() {
            return world;
        }

        private BlockPos getBlockPos() {
            return blockPos;
        }

        public String getKey () {
            return String.format( "%d-%d-%d", blockPos.getX(), blockPos.getY(), blockPos.getZ() );
        }

        private Block getBlock() {
            return world.getBlockState( blockPos ).getBlock();
        }

        public void checkGrowth() {
            long secondsSinceRegistration = this.getSecondsSinceRegistration();
            if (
                // Check if it has been longer then 2 seconds
                secondsSinceRegistration > delay &&
                // Check if it has already been time
                ( secondsSinceRegistration % everyXSeconds) == 0 &&
                // Check if growth was already applied
                secondsSinceRegistration != this.lastSecondGrown
            ) {
                Block block = this.getBlock();
                if (
                    // Check if the block is growable
                    block instanceof IGrowable
                ) {
                    this.lastSecondGrown = secondsSinceRegistration;
                    this.applyGrowth( block );
                }
            }
        }

        public void applyGrowth ( Block block ) {
            IGrowable targetBlock = (IGrowable) block;

            if(targetBlock.isValidBonemealTarget(world, blockPos, world.getBlockState(blockPos), world.isClientSide)) {
                // Extract values
                ServerWorld world = getWorld();
                BlockPos blockPos = getBlockPos();
                BlockState blockState = world.getBlockState( blockPos );

                // Apply growth effect
                world.sendParticles(
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

                // Grow block
                targetBlock.performBonemeal( world, world.random, blockPos, blockState);
            }
        }

    }

}
