package dev.tmp.StareTillTheyGrow.Actions.Entity;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.gameevent.GameEvent;

public class FallInLoveAction extends AbstractEntityAction {
    public FallInLoveAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    public void invoke() {
        if (entity instanceof Animal animal) {
            if (animal.getAge() == 0 && animal.canFallInLove()) {
                animal.setInLove(player);
                emitParticles(entity.position());
                dimension.gameEvent(entity, GameEvent.ENTITY_INTERACT, new BlockPos(entity.position()));
            }
        }
    }
}
