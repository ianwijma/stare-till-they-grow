package dev.tmp.StareTillTheyGrow.Actions.Block;

import dev.tmp.StareTillTheyGrow.Actions.AbstractAction;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.core.BlockPos;

abstract public class AbstractBlockAction extends AbstractAction {
    protected BlockPos blockPos;

    public AbstractBlockAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
        blockPos = playerBlockTarget.getTarget();
    }
}
