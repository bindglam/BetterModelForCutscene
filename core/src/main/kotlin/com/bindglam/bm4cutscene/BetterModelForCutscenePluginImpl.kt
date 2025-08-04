package com.bindglam.bm4cutscene

import com.bindglam.bm4cutscene.manager.*
import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.*
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BetterModelForCutscenePluginImpl : JavaPlugin(), BetterModelForCutscenePlugin {
    private val cutsceneManager = CutsceneManagerImpl(this)

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))

        CommandAPICommand("bettermodelforcutscene")
            .withAliases("bm4cutscene")
            .withPermission(CommandPermission.OP)
            .withSubcommands(
                CommandAPICommand("play")
                    .withArguments(PlayerArgument("player"), LocationArgument("location"), StringArgument("id"), StringArgument("animation"),
                        BooleanArgument("shiftToClose").setOptional(true))
                    .executes(CommandExecutor { sender, args ->
                        val player = args["player"] as Player
                        val location = args["location"] as Location
                        val id = args["id"] as String
                        val animation = args["animation"] as String

                        cutsceneManager.playCutscene(player, location, id, animation).apply {
                            if(args.get("shiftToClose") != null)
                                shiftToClose(args["shiftToClose"] as Boolean)
                        }
                    }),
                CommandAPICommand("stop")
                    .withArguments(PlayerArgument("player"))
                    .executes(CommandExecutor { sender, args ->
                        val player = args["player"] as Player

                        cutsceneManager.stopCutscene(player)
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

    override fun getCutsceneManager(): CutsceneManager = cutsceneManager
}