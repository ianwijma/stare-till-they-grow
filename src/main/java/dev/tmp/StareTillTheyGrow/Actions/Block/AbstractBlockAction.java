package dev.tmp.StareTillTheyGrow.Actions.Block;

import dev.tmp.StareTillTheyGrow.Actions.ActionInterface;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

abstract public class AbstractBlockAction implements ActionInterface {
    protected ServerLevel dimension;
    protected BlockPos blockPos;
    protected Player player;

    public AbstractBlockAction(PlayerTargetDictionary.PlayerBlockTarget playerBlockTarget) {
        dimension = playerBlockTarget.getDimension();
        blockPos = playerBlockTarget.getTarget();
        player = playerBlockTarget.getPlayer();
    }
}
