package dev.tmp.StareTillTheyGrow.Actions.Entity;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.animal.Animal;

public class GrowUpAction extends AbstractEntityAction {
    public GrowUpAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    public void invoke() {
        if (entity instanceof Animal animal) {
            if (animal.isBaby()) {
                animal.ageUp(60 * 5);
            }
        }
    }
}
