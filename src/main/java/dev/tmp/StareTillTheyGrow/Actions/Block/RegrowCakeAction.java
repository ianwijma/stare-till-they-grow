package dev.tmp.StareTillTheyGrow.Actions.Block;

import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RegrowCakeAction extends AbstractBlockAction {
    public RegrowCakeAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
    }

    @Override
    public void invoke() {
        BlockState blockState = dimension.getBlockState(blockPos);
        if (
            isEnabledInConfig
            && !isBlocked
            && blockState.getBlock() instanceof CakeBlock
        ) {
            int bites = blockState.getValue(CakeBlock.BITES);
            if (bites > 0) {
                dimension.setBlock(blockPos, blockState.setValue(CakeBlock.BITES, bites - 1), 3);
                emitParticles(blockPos);
            }
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.COMMON.enableRegrowCake.get();
    }
}
