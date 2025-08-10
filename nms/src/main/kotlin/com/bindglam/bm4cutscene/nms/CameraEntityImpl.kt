package com.bindglam.bm4cutscene.nms

import com.bindglam.bm4cutscene.cutscene.CameraEntity
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.GameType
import org.bukkit.Location
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.entity.Player

class CameraEntityImpl(private val player: Player, private var location: Location) : CameraEntity {
    private val lastGameMode = player.handle.gameMode.gameModeForPlayer

    private val display = Display.ItemDisplay(EntityType.ITEM_DISPLAY, (location.world as CraftWorld).handle).apply { moveTo(location.x, location.y, location.z, location.yaw, location.pitch) }

    override fun spawn() {
        player.sendPacket(display.generateAddPacket())
    }

    override fun update() {
        player.sendPacket(display.generateUpdatePacket())
    }

    override fun remove() {
        player.sendPacket(display.generateRemovePacket())
    }

    override fun moveDuration(duration: Int) {
        display.entityData.set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, duration)
    }

    override fun attachPlayer() {
        player.sendPacket(arrayListOf<Packet<*>>(ClientboundSetCameraPacket(display))
            .apply { addAll(player.generateGameModePacket(GameType.SPECTATOR)) })
    }

    override fun detachPlayer() {
        player.sendPacket(arrayListOf<Packet<*>>(ClientboundSetCameraPacket(player.handle))
            .apply { addAll(player.generateGameModePacket(lastGameMode)) })
    }

    override fun getId(): Int = display.id
    override fun getLocation(): Location = location

    override fun setLocation(location: Location) {
        this.location = location

        display.moveTo(location.x, location.y, location.z, location.yaw, location.pitch)
    }
}