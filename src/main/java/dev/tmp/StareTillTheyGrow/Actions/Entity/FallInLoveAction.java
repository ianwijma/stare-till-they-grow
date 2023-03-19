package dev.tmp.StareTillTheyGrow.Actions.Entity;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class FallInLoveAction extends AbstractEntityAction {
    public FallInLoveAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    @Override
    public void invoke() {
        if (
            isEnabledInConfig
            && entity instanceof Animal animal
            && animal.getAge() == 0
            && animal.canFallInLove()
        ) {
            animal.setInLove(player);
            emitParticles(entity.position());
            Vec3 position = entity.position();
            dimension.gameEvent(entity, GameEvent.ENTITY_INTERACT, new BlockPos((int) position.x, (int) position.y, (int) position.z));
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.COMMON.enableFallInLove.get();
    }
}
