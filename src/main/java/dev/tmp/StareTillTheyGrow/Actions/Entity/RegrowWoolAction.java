package dev.tmp.StareTillTheyGrow.Actions.Entity;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.animal.Sheep;

public class RegrowWoolAction extends AbstractEntityAction {
    public RegrowWoolAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    public void invoke() {
        if (entity instanceof Sheep sheep) {
            if (sheep.isSheared()) {
                sheep.setSheared(false);
            }
        }
    }
}
