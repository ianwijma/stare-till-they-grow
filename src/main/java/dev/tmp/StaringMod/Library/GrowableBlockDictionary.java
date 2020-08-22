package dev.tmp.StaringMod.Library;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.function.BiConsumer;

public class GrowableBlockDictionary {

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

    public static void ensure () {
        HASHTABLE.forEach((__, worldBlockPos) -> {
            if ( !worldBlockPos.isGrowable() ) {
                unregister( worldBlockPos );
            }
        });
    }

    public static void forEach (BiConsumer<String, WorldBlockPos> worldBlockPosConsumer ) {
        HASHTABLE.forEach( worldBlockPosConsumer );
    }

    public static class WorldBlockPos {

        private final ServerWorld world;
        private final BlockPos blockPos;

        public WorldBlockPos ( ServerWorld world, BlockPos blockPos ) {
            this.world = world;
            this.blockPos = blockPos;
        }

        public ServerWorld getWorld() {
            return world;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public String getKey () {
            return String.format( "%d-%d-%d", blockPos.getX(), blockPos.getY(), blockPos.getZ() );
        }

        public boolean isGrowable() {
            BlockState blockState = world.getBlockState( blockPos );
            Block block = blockState.getBlock();
            if ( block instanceof IGrowable ) {
                IGrowable iGrowable = (IGrowable) block;
                return iGrowable.canGrow( world, blockPos, blockState, false );
            }

            // Is not growable
            return false;
        }

        public Block getBlock() {
            return world.getBlockState( blockPos ).getBlock();
        }
    }

}
