package dev.tmp.StareTillTheyGrow.Library;

import dev.tmp.StareTillTheyGrow.Config.Config;

public class ActionTypeHelper {
    public static boolean actionTypeEnabled(ActionType actionType) {
        return actionType == ActionType.BONE_MEAL_BLOCK && Config.COMMON.applyBoneMeal.get() ||
                actionType == ActionType.REGROW_WOOL && Config.COMMON.regrowWool.get() ||
                actionType == ActionType.GROW_UP && Config.COMMON.growBabies.get();
    }
}
