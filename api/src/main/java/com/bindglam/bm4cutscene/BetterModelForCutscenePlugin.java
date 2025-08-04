package com.bindglam.bm4cutscene;

import com.bindglam.bm4cutscene.manager.CutsceneManager;
import org.jetbrains.annotations.NotNull;

public interface BetterModelForCutscenePlugin {
    @NotNull CutsceneManager getCutsceneManager();
}
