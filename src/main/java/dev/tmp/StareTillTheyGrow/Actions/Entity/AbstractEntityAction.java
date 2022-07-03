package dev.tmp.StareTillTheyGrow.Actions.Entity;

import dev.tmp.StareTillTheyGrow.Actions.ActionInterface;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

abstract public class AbstractEntityAction implements ActionInterface {
    protected ServerLevel dimension;
    protected Entity entity;
    protected Player player;

    public AbstractEntityAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        dimension = playerEntityTarget.getDimension();
        entity = playerEntityTarget.getTarget();
        player = playerEntityTarget.getPlayer();
    }
}
