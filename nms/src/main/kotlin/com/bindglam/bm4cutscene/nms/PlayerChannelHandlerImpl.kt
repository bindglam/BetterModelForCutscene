package com.bindglam.bm4cutscene.nms

import com.bindglam.bm4cutscene.BetterModelForCutscene
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import org.bukkit.entity.Player

class PlayerChannelHandlerImpl(private val player: Player) : PlayerChannelHandler, ChannelDuplexHandler() {
    companion object {
        private const val INJECT_NAME = "bm4cutscene_channel_handler"
    }

    private val connection = player.handle.connection

    init {
        val pipeline = connection.connection.channel.pipeline()
        pipeline.toMap().forEach {
            if (it.value is Connection) pipeline.addBefore(it.key, INJECT_NAME, this)
        }
    }

    override fun close() {
        val channel = connection.connection.channel
        channel.eventLoop().submit {
            channel.pipeline().remove(INJECT_NAME)
        }
    }

    private fun <T : ServerGamePacketListener> Packet<in T>.handle(): Packet<in T>? {
        when (this) {
            is ServerboundInteractPacket -> {
                if(BetterModelForCutscene.getInstance().cutsceneManager.getCutscene(player) != null)
                    return null
            }
        }
        return this
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        super.write(ctx, msg, promise)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        super.channelRead(ctx, if (msg is Packet<*>) msg.handle() ?: return else msg)
    }
}