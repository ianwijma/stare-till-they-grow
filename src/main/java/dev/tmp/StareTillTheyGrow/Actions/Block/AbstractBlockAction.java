package dev.tmp.StareTillTheyGrow.Actions.Block;

import dev.tmp.StareTillTheyGrow.Actions.AbstractAction;
import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

abstract public class AbstractBlockAction extends AbstractAction {
    protected BlockPos blockPos;
    protected boolean isBlocked;

    public AbstractBlockAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
        blockPos = playerBlockTarget.getTarget();
        isBlocked = getIsBlocked();
    }

    private boolean getIsBlocked() {
        BlockState blockState = dimension.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ResourceLocation tag = ForgeRegistries.BLOCKS.getKey(block);

        boolean isBlocklist = Config.COMMON.isBlockList.get();
        String[] filterList = Arrays.copyOf(
            Config.COMMON.blockOrAllowList.get().toArray(),
            Config.COMMON.blockOrAllowList.get().size(),
            String[].class
        );

        boolean isBlocked = true;
        if (tag != null) {
            isBlocked = ArrayUtils.contains(filterList, tag.toString());
            if (!isBlocklist) {
                isBlocked = !isBlocked;
            }
        }

        return isBlocked;
    }
}
