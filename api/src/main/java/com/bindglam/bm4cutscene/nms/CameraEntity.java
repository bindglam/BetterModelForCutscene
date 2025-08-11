package com.bindglam.bm4cutscene.nms;

import org.bukkit.Location;

public interface CameraEntity {
    void spawn();

    void update();

    void remove();

    void moveDuration(int duration);

    void attachPlayer();

    void detachPlayer();

    int getId();

    Location getLocation();

    void setLocation(Location location);
}
