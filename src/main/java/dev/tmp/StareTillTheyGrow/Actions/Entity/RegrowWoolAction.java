package dev.tmp.StareTillTheyGrow.Actions.Entity;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.animal.Sheep;

public class RegrowWoolAction extends AbstractEntityAction {
    public RegrowWoolAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    @Override
    public void invoke() {
        if (
            isEnabledInConfig
            && entity instanceof Sheep sheep
            && sheep.isSheared()
        ) {
            sheep.setSheared(false);
            emitParticles(entity.position());
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.COMMON.enableRegrowWool.get();
    }
}
