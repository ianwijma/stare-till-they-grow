package dev.tmp.StareTillTheyGrow.Actions.Entity;

import dev.tmp.StareTillTheyGrow.Actions.AbstractAction;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.PlayerEntityTarget;
import dev.tmp.StareTillTheyGrow.Utilities.ForgeLogger;
import net.minecraft.world.entity.Entity;

abstract public class AbstractEntityAction extends AbstractAction {
    protected Entity entity;

    public AbstractEntityAction(PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
        entity = playerEntityTarget.getTarget();

        ForgeLogger.LOGGER.debug("[ACTION] init entity action");
    }
}
