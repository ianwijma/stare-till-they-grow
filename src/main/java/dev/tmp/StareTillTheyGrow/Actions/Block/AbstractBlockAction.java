package dev.tmp.StareTillTheyGrow.Actions.Block;

import dev.tmp.StareTillTheyGrow.Actions.AbstractAction;
import dev.tmp.StareTillTheyGrow.Config.Config;
import dev.tmp.StareTillTheyGrow.Dictionaries.PlayerTargetDictionary.PlayerBlockTarget;
import dev.tmp.StareTillTheyGrow.Utilities.ForgeLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

abstract public class AbstractBlockAction extends AbstractAction {
    protected BlockPos blockPos;
    protected boolean isBlocked;

    public AbstractBlockAction(PlayerBlockTarget playerBlockTarget) {
        super(playerBlockTarget);
        blockPos = playerBlockTarget.getTarget();
        isBlocked = getIsBlocked();

        ForgeLogger.LOGGER.debug("[ACTION] init block action");
    }

    private boolean getIsBlocked() {
        BlockState blockState = dimension.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ResourceLocation tag = ForgeRegistries.BLOCKS.getKey(block);
        List<? extends String> blockOrAllowList = Config.COMMON.blockOrAllowList.get();

        boolean isBlocked = true;
        if (tag != null) {
            boolean isBlocklist = Config.COMMON.isBlockList.get();
            String[] filterList = Arrays.copyOf(blockOrAllowList.toArray(), blockOrAllowList.size(), String[].class);

            isBlocked = ArrayUtils.contains(filterList, tag.toString());
            if (!isBlocklist) {
                isBlocked = !isBlocked;
            }
        }

        return isBlocked;
    }
}
