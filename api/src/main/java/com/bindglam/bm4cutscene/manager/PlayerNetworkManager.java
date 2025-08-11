package com.bindglam.bm4cutscene.manager;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerNetworkManager {
    void inject(@NotNull Player player);

    void eject(@NotNull Player player);
}
