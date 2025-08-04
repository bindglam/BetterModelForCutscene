package com.bindglam.bm4cutscene.nms

import com.bindglam.bm4cutscene.cutscene.CameraEntity
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PositionMoveRotation
import net.minecraft.world.entity.Relative
import org.bukkit.Location
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

class CameraEntityImpl(private val player: Player, private var location: Location) : CameraEntity {
    private val display = Display.ItemDisplay(EntityType.ITEM_DISPLAY, (location.world as CraftWorld).handle).apply { moveTo(location.x, location.y, location.z, location.yaw, location.pitch) }

    private val Player.handle: net.minecraft.world.entity.player.Player
        get() = (this as CraftPlayer).handle

    private fun Player.sendPacket(packet: List<Packet<*>>) {
        val connection = (player as CraftPlayer).handle.connection
        packet.forEach { connection.sendPacket(it) }
    }

    private fun Entity.generateAddPacket(): List<Packet<*>> {
        return arrayListOf<Packet<*>>(
            ClientboundAddEntityPacket(display.id, display.uuid, display.x, display.y, display.z, display.xRot, display.yRot, display.type, 0, display.deltaMovement, display.yHeadRot.toDouble()),
        ).apply { addAll(generateUpdatePacket()) }
    }

    private fun Entity.generateUpdatePacket(): List<Packet<*>> {
        val packets = arrayListOf<Packet<*>>(
            ClientboundTeleportEntityPacket(display.id, PositionMoveRotation.of(display), setOf(), false)
        )

        val entityData = display.entityData.nonDefaultValues
        if(entityData != null)
            packets.add(ClientboundSetEntityDataPacket(display.id, entityData))

        return packets
    }

    private fun Entity.generateRemovePacket(): List<Packet<*>> {
        return arrayListOf<Packet<*>>(
            ClientboundRemoveEntitiesPacket(display.id)
        )
    }

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
        player.sendPacket(listOf(
            ClientboundSetCameraPacket(display)
        ))
    }

    override fun detachPlayer() {
        player.sendPacket(listOf(
            ClientboundSetCameraPacket(player.handle)
        ))
    }

    override fun getId(): Int {
        return display.id
    }

    override fun getLocation(): Location {
        return location
    }

    override fun setLocation(location: Location) {
        this.location = location

        display.moveTo(location.x, location.y, location.z, location.yaw, location.pitch)
    }
}