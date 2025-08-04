package com.bindglam.bm4cutscene;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class BetterModelForCutscene {
    private static BetterModelForCutscenePlugin instance;

    private BetterModelForCutscene() {
    }

    public static @NotNull BetterModelForCutscenePlugin getInstance() {
        return instance;
    }

    @ApiStatus.Internal
    static void setInstance(@NotNull BetterModelForCutscenePlugin instance) {
        BetterModelForCutscene.instance = instance;
    }
}
