package dev.tmp.StareTillTheyGrow.Library;

import dev.tmp.StareTillTheyGrow.Config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
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
    static String[] blackList = Arrays.copyOf(Config.COMMON.blackList.get().toArray(), Config.COMMON.blackList.get().size(), String[].class);
    // The whiteList
    static String[] whiteList = Arrays.copyOf(Config.COMMON.whiteList.get().toArray(), Config.COMMON.whiteList.get().size(), String[].class);


    private static final Hashtable<UUID, PlayerTarget> HASHTABLE = new Hashtable<>();

    public static void registerBlock(ActionType actionType, ServerWorld world, PlayerEntity player, BlockPos pos) {
        if (canRegisterBlock(actionType, world, pos)) {
            HASHTABLE.put(player.getUUID(), new PlayerTargetBlock(actionType, world, player, pos));
        } else {
            unregister(player);
        }
    }

    private static boolean canRegisterBlock(ActionType actionType, ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        String name = block.getRegistryName().toString();

        if (ActionTypeHelper.actionTypeEnabled(actionType)) {
            if (useBlackList) {
                return !ArrayUtils.contains(blackList, name);
            }

            return ArrayUtils.contains(whiteList, name);
        }

        return false;
    }

    public static void registerEntity(ActionType actionType, ServerWorld world, PlayerEntity player, Entity entity) {
        if (canRegisterEntity(actionType)) {
            HASHTABLE.put(player.getUUID(), new PlayerTargetEntity(actionType, world, player, entity));
        } else {
            unregister(player);
        }
    }

    private static boolean canRegisterEntity(ActionType actionType) {
        return ActionTypeHelper.actionTypeEnabled(actionType);
    }

    public static void unregister(PlayerEntity player) {
        HASHTABLE.remove(player.getUUID());
    }

    public static void forEach(BiConsumer<UUID, PlayerTarget> consumer) {
        HASHTABLE.forEach(consumer);
    }

    public static class PlayerTarget {

        // The delay before applying growth
        static long delay = Config.COMMON.delay.get();
        // Applies growth every x seconds
        static long everyXSeconds = Config.COMMON.everyXSeconds.get();

        final ActionType actionType;
        final ServerWorld world;
        final PlayerEntity player;
        // When the class was created
        private final Date createdDate;
        // When the last time it ticked
        private long lastTimeTicked;

        public PlayerTarget(ActionType actionType, ServerWorld world, PlayerEntity player) {
            this.actionType = actionType;
            this.world = world;
            this.player = player;
            this.createdDate = new Date();
        }

        private long getSecondsSinceRegistration() {
            long created = this.createdDate.getTime();
            long now = (new Date()).getTime();
            return (now - created) / 1000;
        }

        public void handle() {
            if (canTick()) {
                tick();
            }
        }

        private boolean canTick() {
            long secondsSinceRegistration = this.getSecondsSinceRegistration();
            if (
                // Check if it has been longer then 2 seconds
                    secondsSinceRegistration > delay &&
                            // Check if it has already been time
                            (secondsSinceRegistration % everyXSeconds) == 0 &&
                            // Check if growth was already applied
                            secondsSinceRegistration != this.lastTimeTicked
            ) {
                this.lastTimeTicked = secondsSinceRegistration;
                return true;
            }
            return false;
        }

        protected void tick() {
        }

        protected void showParticles(double posX, double posY, double posZ) {
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

        public PlayerTargetBlock(ActionType actionType, ServerWorld world, PlayerEntity player, BlockPos pos) {
            super(actionType, world, player);
            this.pos = pos;
        }

        protected void tick() {
            if (this.actionType == ActionType.BONE_MEAL_BLOCK) {
                this.applyBoneMeal();
            }
        }

        private void applyBoneMeal() {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof IGrowable) {
                IGrowable targetBlock = (IGrowable) block;
                if (targetBlock.isValidBonemealTarget(world, pos, state, world.isClientSide)) {
                    showParticles(pos.getX(), pos.getY(), pos.getZ());
                    targetBlock.performBonemeal(world, world.random, pos, state);
                }
            }
        }
    }

    public static class PlayerTargetEntity extends PlayerTarget {
        final Entity entity;

        public PlayerTargetEntity(ActionType actionType, ServerWorld world, PlayerEntity player, Entity entity) {
            super(actionType, world, player);
            this.entity = entity;
        }

        protected void tick() {
            if (this.actionType == ActionType.GROW_UP) {
                this.applyGrowth();
            } else if (this.actionType == ActionType.REGROW_WOOL) {
                this.applyRegrowWool();
            }
        }

        private void applyGrowth() {
            if (entity instanceof AnimalEntity) {
                AnimalEntity targetEntity = (AnimalEntity) entity;
                if (targetEntity.isBaby()) {
                    Vector3d pos = targetEntity.position();
                    showParticles(pos.x, pos.y, pos.z);
                    targetEntity.ageUp(60 * 5);
                }
            }
        }

        private void applyRegrowWool() {
            if (entity instanceof SheepEntity) {
                SheepEntity targetEntity = (SheepEntity) entity;
                if (targetEntity.isSheared()) {
                    Vector3d pos = targetEntity.position();
                    showParticles(pos.x, pos.y, pos.z);
                    targetEntity.setSheared(false);
                }
            }
        }
    }
}
