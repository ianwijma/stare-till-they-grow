package dev.tmp.StareTillTheyGrow.Actions.Block;

import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ApplyBoneMealAction extends AbstractBlockAction {
    public ApplyBoneMealAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
    }

    public void invoke() {
        BlockState blockState = dimension.getBlockState(blockPos);
        if (
            blockState.getBlock() instanceof BonemealableBlock bonemealableBlock
            && bonemealableBlock.isValidBonemealTarget(dimension, blockPos, blockState, dimension.isClientSide)
        ) {
            bonemealableBlock.performBonemeal(dimension, dimension.random, blockPos, blockState);
        }
    }
}
