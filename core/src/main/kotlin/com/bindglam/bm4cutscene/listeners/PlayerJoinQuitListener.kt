package com.bindglam.bm4cutscene.listeners

import com.bindglam.bm4cutscene.BetterModelForCutscene
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerJoinQuitListener : Listener {
    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        BetterModelForCutscene.getInstance().playerNetworkManager.inject(player)
    }

    @EventHandler
    fun PlayerQuitEvent.onJoin() {
        BetterModelForCutscene.getInstance().playerNetworkManager.eject(player)
    }
}