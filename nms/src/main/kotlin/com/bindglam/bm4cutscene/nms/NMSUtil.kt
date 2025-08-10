package com.bindglam.bm4cutscene.nms

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundGameEventPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.PositionMoveRotation
import net.minecraft.world.level.GameType
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import kotlin.collections.forEach

val Player.handle: ServerPlayer
    get() = (this as CraftPlayer).handle

fun Player.sendPacket(packet: List<Packet<*>>) {
    val connection = handle.connection
    packet.forEach { connection.sendPacket(it) }
}

fun Player.generateGameModePacket(gameMode: GameType): List<Packet<*>> {
    return arrayListOf(
        ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, gameMode.id.toFloat())
    )
}

fun Entity.generateAddPacket(): List<Packet<*>> {
    return arrayListOf<Packet<*>>(
        ClientboundAddEntityPacket(id, uuid, x, y, z, xRot, yRot, type, 0, deltaMovement, yHeadRot.toDouble()),
    ).apply { addAll(generateUpdatePacket()) }
}

fun Entity.generateUpdatePacket(): List<Packet<*>> {
    val packets = arrayListOf<Packet<*>>(
        ClientboundTeleportEntityPacket(id, PositionMoveRotation.of(this), setOf(), false)
    )

    val entityData = entityData.nonDefaultValues
    if(entityData != null)
        packets.add(ClientboundSetEntityDataPacket(id, entityData))

    return packets
}

fun Entity.generateRemovePacket(): List<Packet<*>> {
    return arrayListOf<Packet<*>>(
        ClientboundRemoveEntitiesPacket(id)
    )
}