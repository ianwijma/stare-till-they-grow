package dev.tmp.StareTillTheyGrow.Library;

import dev.tmp.StareTillTheyGrow.Config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;
import java.util.function.BiConsumer;

public class PlayerTargetDictionary {

    // To either use the blackList or whiteList
    static boolean useBlackList = Config.COMMON.useBlackList.get();
    // The blackList
    static String[] blackList = Arrays.copyOf( Config.COMMON.blackList.get().toArray(), Config.COMMON.blackList.get().size(), String[].class );
    // The whiteList
    static String[] whiteList = Arrays.copyOf( Config.COMMON.whiteList.get().toArray(), Config.COMMON.whiteList.get().size(), String[].class );


    private static final Hashtable<UUID, PlayerTarget> HASHTABLE = new Hashtable<>();

    public static void registerBlock(ActionType actionType, ServerLevel world, Player player, BlockPos pos) {
        if(canRegisterBlock(world, pos)) {
            HASHTABLE.put(player.getUUID(), new PlayerTargetBlock(actionType, world, player, pos));
        } else {
            unregister(player);
        }
    }

    private static boolean canRegisterBlock(ServerLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        String name = block.getRegistryName().toString();

        if (useBlackList) {
            return !ArrayUtils.contains( blackList, name );
        }

        return ArrayUtils.contains( whiteList, name );
    }

    public static void registerEntity(ActionType type, ServerLevel world, Player player, Entity entity) {
        if(canRegisterEntity(world, entity)) {
            HASHTABLE.put(player.getUUID(), new PlayerTargetEntity(type, world, player, entity));
        } else {
            unregister(player);
        }
    }

    private static boolean canRegisterEntity(ServerLevel world, Entity entity) {
        return false;
    }

    public static void unregister(Player player) {
        HASHTABLE.remove(player.getUUID());
    }

    public static void forEach (BiConsumer<UUID, PlayerTarget> consumer ) {
        HASHTABLE.forEach( consumer );
    }

    public static class PlayerTarget{

        // The delay before applying growth
        static long delay = Config.COMMON.delay.get();
        // Applies growth every x seconds
        static long everyXSeconds = Config.COMMON.everyXSeconds.get();

        final ActionType actionType;
        final ServerLevel world;
        final Player player;
        // When the class was created
        private final Date createdDate;
        // When the last time it ticked
        private long lastTimeTicked;

        public PlayerTarget(ActionType actionType, ServerLevel world, Player player) {
            this.actionType = actionType;
            this.world = world;
            this.player = player;
            this.createdDate = new Date();
        }

        private long getSecondsSinceRegistration() {
            long created = this.createdDate.getTime();
            long now = (new Date()).getTime();
            return (now-created)/1000;
        }

        public void handle() {
            if(canTick()) {
                tick();
            }
        }

        private boolean canTick() {
            long secondsSinceRegistration = this.getSecondsSinceRegistration();
            if(
                // Check if it has been longer then 2 seconds
                secondsSinceRegistration > delay &&
                // Check if it has already been time
                ( secondsSinceRegistration % everyXSeconds) == 0 &&
                // Check if growth was already applied
                secondsSinceRegistration != this.lastTimeTicked
            ) {
                this.lastTimeTicked = secondsSinceRegistration;
                return true;
            }
            return false;
        }

        protected void tick(){}

        protected void showParticles(int posX, int posY, int posZ) {
            world.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    posX,
                    posY,
                    posZ,
                    75,
                    1,
                    1,
                    1,
                    1
            );
        }
    }

    public static class PlayerTargetBlock extends PlayerTarget {
        private final BlockPos pos;
        public PlayerTargetBlock(ActionType actionType, ServerLevel world, Player player, BlockPos pos) {
            super(actionType, world, player);
            this.pos = pos;
        }

        protected void tick(){
            if(this.actionType == ActionType.BONE_MEAL_BLOCK) {
                this.applyBoneMeal();
            }
        }

        private void applyBoneMeal() {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if(block instanceof BonemealableBlock) {
                BonemealableBlock targetBlock = (BonemealableBlock) block;
                if(targetBlock.isValidBonemealTarget(world, pos, state, world.isClientSide)) {
                    showParticles(pos.getX(), pos.getY(), pos.getZ());
                    targetBlock.performBonemeal(world, world.random, pos, state);
                }
            }
        }
    }

    public static class PlayerTargetEntity extends PlayerTarget {
        final Entity entity;
        public PlayerTargetEntity(ActionType actionType, ServerLevel world, Player player, Entity entity) {
            super(actionType, world, player);
            this.entity = entity;
        }

        protected void tick(){

        }
    }
}
