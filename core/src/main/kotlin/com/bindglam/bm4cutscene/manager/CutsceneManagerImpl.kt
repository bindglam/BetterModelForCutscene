package com.bindglam.bm4cutscene.manager

import com.bindglam.bm4cutscene.cutscene.Cutscene
import com.bindglam.bm4cutscene.cutscene.CutsceneImpl
import com.bindglam.bm4cutscene.cutscene.CutsceneProperties
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CutsceneManagerImpl(private val plugin: Plugin) : CutsceneManager {
    private val cutsceneMap = ConcurrentHashMap<UUID, Cutscene>()

    override fun playCutscene(player: Player, location: Location, properties: CutsceneProperties): Cutscene {
        stopCutscene(player)

        return CutsceneImpl(plugin, player, location, properties).also { cutsceneMap[player.uniqueId] = it }
    }

    override fun stopCutscene(player: Player) {
        cutsceneMap.remove(player.uniqueId)?.close()
    }

    override fun getCutscene(player: Player): Cutscene? = cutsceneMap[player.uniqueId]
}