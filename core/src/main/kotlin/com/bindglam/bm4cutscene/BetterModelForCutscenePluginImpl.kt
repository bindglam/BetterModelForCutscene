package com.bindglam.bm4cutscene

import com.bindglam.bm4cutscene.cutscene.CutsceneImpl
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.LocationArgument
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BetterModelForCutscenePluginImpl : JavaPlugin(), BetterModelForCutscenePlugin {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))

        CommandAPICommand("bettermodelforcutscene")
            .withAliases("bm4cutscene")
            .withPermission(CommandPermission.OP)
            .withSubcommands(
                CommandAPICommand("play")
                    .withArguments(PlayerArgument("player"), LocationArgument("location"), StringArgument("id"), StringArgument("animation"))
                    .executes(CommandExecutor { sender, args ->
                        val player = args["player"] as Player
                        val location = args["location"] as Location
                        val id = args["id"] as String
                        val animation = args["animation"] as String

                        CutsceneImpl(this, player, location, id, animation)
                    })
            )
            .register()
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        BetterModelForCutscene.setInstance(this)
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }
}