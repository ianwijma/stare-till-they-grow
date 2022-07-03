package dev.tmp.StareTillTheyGrow.Dictionaries;

import dev.tmp.StareTillTheyGrow.Config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Hashtable;
import java.util.UUID;
import java.util.function.BiConsumer;


public class PlayerTargetDictionary {
    private static final Hashtable<Player, PlayerTarget> DICTIONARY = new Hashtable<>();

    public static void registerBlock(Player player, ServerLevel dimension, BlockPos position) {
        DICTIONARY.put(player, new PlayerBlockTarget(player, dimension, position));
    }

    public static void registerEntity(Player player, ServerLevel dimension, Entity entity) {
        DICTIONARY.put(player, new PlayerEntityTarget(player, dimension, entity));
    }

    public static void registerEntity(Player player, ServerLevel dimension, UUID entityUuid) {
        Entity entity = dimension.getEntity(entityUuid);
        DICTIONARY.put(player, new PlayerEntityTarget(player, dimension, entity));
    }

    public static void unregister(Player player) {
        DICTIONARY.remove(player);
    }

    public static void forEach(BiConsumer<Player, PlayerTarget> consumer) {
        DICTIONARY.forEach(consumer);
    }

    public static abstract class PlayerTarget {
        protected final Player player;
        protected final ServerLevel dimension;
        protected int internalTick = 0;

        public PlayerTarget(Player player, ServerLevel dimension) {
            this.player = player;
            this.dimension = dimension;
        }

        public Player getPlayer() {
            return this.player;
        }

        public ServerLevel getDimension() {
            return this.dimension;
        }

        public void tick() {
            internalTick++;
        }

        public boolean canInvoke() {
            return internalTick > Config.COMMON.ticksDelay.get() && internalTick % Config.COMMON.ticksBetween.get() == 0;
        }
    }

    public static class PlayerBlockTarget extends PlayerTarget {
        private final BlockPos target;

        public PlayerBlockTarget(Player player, ServerLevel dimension, BlockPos target) {
            super(player, dimension);
            this.target = target;
        }

        public BlockPos getTarget() {
            return target;
        }
    }

    public static class PlayerEntityTarget extends PlayerTarget {
        private final Entity target;

        public PlayerEntityTarget(Player player, ServerLevel dimension, Entity target) {
            super(player, dimension);
            this.target = target;
        }

        public Entity getTarget() {
            return target;
        }
    }
}
