package com.bindglam.bm4cutscene.cutscene;

import kr.toxicity.model.api.bone.RenderedBone;
import kr.toxicity.model.api.tracker.Tracker;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Cutscene {
    void close();

    @NotNull Location getLocation();

    @NotNull Tracker getTracker();

    @NotNull RenderedBone getCamera();

    @NotNull CameraEntity getCameraEntity();
}
