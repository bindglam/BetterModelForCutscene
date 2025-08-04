package com.bindglam.bm4cutscene.manager

import com.bindglam.bm4cutscene.cutscene.Cutscene
import com.bindglam.bm4cutscene.cutscene.CutsceneImpl
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

class CutsceneManagerImpl(private val plugin: Plugin) : CutsceneManager {
    private val cutsceneMap = hashMapOf<UUID, Cutscene>()

    override fun playCutscene(
        player: Player,
        location: Location,
        model: String,
        animation: String
    ): Cutscene {
        val cutscene = CutsceneImpl(plugin, player, location, model, animation)
        cutsceneMap[player.uniqueId] = cutscene
        return cutscene
    }

    override fun stopCutscene(player: Player) {
        if(!cutsceneMap.contains(player.uniqueId))
            return

        cutsceneMap[player.uniqueId]!!.close()
        cutsceneMap.remove(player.uniqueId)
    }

    override fun getCutscene(player: Player): Cutscene? {
        return cutsceneMap[player.uniqueId]
    }
}