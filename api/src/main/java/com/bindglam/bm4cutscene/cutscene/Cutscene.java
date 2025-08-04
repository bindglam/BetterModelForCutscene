package com.bindglam.bm4cutscene.cutscene;

import kr.toxicity.model.api.bone.RenderedBone;
import kr.toxicity.model.api.tracker.EntityTracker;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Cutscene {
    @NotNull Location getLocation();

    @NotNull EntityTracker getTracker();

    @NotNull RenderedBone getCamera();

    @NotNull CameraEntity getCameraEntity();
}
