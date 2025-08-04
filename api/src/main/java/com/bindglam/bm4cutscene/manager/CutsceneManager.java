package com.bindglam.bm4cutscene.manager;

import com.bindglam.bm4cutscene.cutscene.Cutscene;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CutsceneManager {
    @NotNull Cutscene playCutscene(@NotNull Player player, @NotNull Location location, @NotNull String model, @NotNull String animation);

    void stopCutscene(@NotNull Player player);

    @Nullable Cutscene getCutscene(@NotNull Player player);
}
