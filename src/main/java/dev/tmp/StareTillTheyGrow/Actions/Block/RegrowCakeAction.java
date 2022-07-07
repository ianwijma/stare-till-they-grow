package dev.tmp.StareTillTheyGrow.Actions.Block;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RegrowCakeAction extends AbstractBlockAction {
    public RegrowCakeAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
    }

    public void invoke() {
        BlockState blockState = dimension.getBlockState(blockPos);
        if (!isBlocked && blockState.getBlock() instanceof CakeBlock) {
            int bites = blockState.getValue(CakeBlock.BITES);
            if (bites > 0) {
                dimension.setBlock(blockPos, blockState.setValue(CakeBlock.BITES, bites - 1), 3);
                emitParticles(blockPos);
            }
        }
    }
}
