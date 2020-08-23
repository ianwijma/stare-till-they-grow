package dev.tmp.StaringMod.Library;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.function.BiConsumer;

public class GrowableBlockDictionary {

    // The delay before applying growth
    static long delayInSeconds = 4;
    // Applies growth every x seconds
    static long applyEveryXSeconds = 2;

    private static final Hashtable<String, WorldBlockPos> HASHTABLE = new Hashtable<String, WorldBlockPos>();

    public static boolean register ( ServerWorld world, BlockPos blockPos ) {
        return register( new WorldBlockPos( world, blockPos ) );
    }
    public static boolean register ( WorldBlockPos worldBlockPos ) {
        System.out.println( "Registered:" );
        System.out.println( worldBlockPos.getBlock().toString() );
        System.out.println( HASHTABLE.size() );

        String key = worldBlockPos.getKey();

        if ( !HASHTABLE.containsKey( key ) ) {
            HASHTABLE.put( key, worldBlockPos );
            return true;
        }

        return false;
    }

    public static boolean unregister ( ServerWorld world, BlockPos blockPos ) {
        return unregister( new WorldBlockPos( world, blockPos ) );
    }
    public static boolean unregister ( WorldBlockPos worldBlockPos ) {
        System.out.println( "Unregistered:" );
        System.out.println( worldBlockPos.getBlock().toString() );
        System.out.println( HASHTABLE.size() );

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
                secondsSinceRegistration > delayInSeconds &&
                // Check if it has already been time
                ( secondsSinceRegistration % applyEveryXSeconds ) == 0 &&
                // Check if growth was already applied
                secondsSinceRegistration != this.lastSecondGrown
            ) {
                this.lastSecondGrown = secondsSinceRegistration;
                this.applyGrowth();
            }
        }

        public void applyGrowth () {
            ServerWorld world = getWorld();
            BlockPos blockPos = getBlockPos();
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
                System.out.println("Growable.grow");
            }
        }

    }

}
