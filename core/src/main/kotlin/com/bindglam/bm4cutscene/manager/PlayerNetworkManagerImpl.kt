package com.bindglam.bm4cutscene.manager

import com.bindglam.bm4cutscene.nms.PlayerChannelHandler
import com.bindglam.bm4cutscene.nms.PlayerChannelHandlerImpl
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerNetworkManagerImpl : PlayerNetworkManager {
    private val channelHandlers = ConcurrentHashMap<UUID, PlayerChannelHandler>()

    override fun inject(player: Player) {
        channelHandlers.computeIfAbsent(player.uniqueId) { PlayerChannelHandlerImpl(player) }
    }

    override fun eject(player: Player) {
        channelHandlers.remove(player.uniqueId)?.close()
    }
}