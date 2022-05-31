package dev.tmp.StareTillTheyGrow.Library;

import dev.tmp.StareTillTheyGrow.Config.Config;
import net.minecraftforge.common.ForgeConfigSpec;

public enum ActionType {
    FALL_IN_LOVE(Config.COMMON.fallInLove),
    BONE_MEAL_BLOCK(Config.COMMON.applyBoneMeal),
    REGROW_WOOL(Config.COMMON.regrowWool),
    GROW_UP(Config.COMMON.growBabies),
    CAKE_REGROWTH(Config.COMMON.cakeRegrowth);

    private final ForgeConfigSpec.BooleanValue configValue;

    ActionType(ForgeConfigSpec.BooleanValue configValue) {
        this.configValue = configValue;
    }

    public boolean enabled() {
        return configValue.get();
    }

    }

