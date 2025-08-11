package com.bindglam.bm4cutscene.cutscene

import com.bindglam.bm4cutscene.nms.CameraEntity
import com.bindglam.bm4cutscene.nms.CameraEntityImpl
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kr.toxicity.model.api.BetterModel
import kr.toxicity.model.api.animation.AnimationIterator
import kr.toxicity.model.api.animation.AnimationModifier
import kr.toxicity.model.api.bone.RenderedBone
import kr.toxicity.model.api.tracker.DummyTracker
import kr.toxicity.model.api.tracker.Tracker
import kr.toxicity.model.api.tracker.TrackerModifier
import kr.toxicity.model.api.tracker.TrackerUpdateAction
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

class CutsceneImpl(private val plugin: Plugin, private val player: Player, private val location: Location, private val properties: CutsceneProperties) : Cutscene {
    private val lastLocation = player.location

    private val model: DummyTracker = BetterModel.model(properties.modelId)
        .map { r ->
            r.create(location, properties.modifier()) { tracker ->
                tracker.update(TrackerUpdateAction.brightness(15, 15))
                tracker.animate(properties.animation, AnimationModifier.DEFAULT_WITH_PLAY_ONCE) {
                    if(!properties.shiftToClose)
                        close()
                }
                tracker.spawn(player)
            }
        }
        .get()
    private val camera: RenderedBone = model.bone("camera")!!
    private val cameraEntity: CameraEntity = CameraEntityImpl(player, cameraLocation()).apply { spawn() }

    private val isRunningAnimation: Boolean
        get() {
            val runningAnimation = model.pipeline.runningAnimation()

            return runningAnimation != null && runningAnimation.type == AnimationIterator.Type.PLAY_ONCE
        }

    private val tickTask: ScheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { task ->
        val isRunning = isRunningAnimation

        if(isRunning) {
            cameraEntity.moveDuration(camera.interpolationDuration())
            cameraEntity.location = cameraLocation()
            cameraEntity.update()

            cameraEntity.attachPlayer()
        }

        if(!isRunning && properties.shiftToClose) {
            player.sendActionBar(Component.keybind("key.sneak").append(Component.text("로 나가기")))

            if(player.isSneaking)
                close()
        }
    }, 50L, 50L, TimeUnit.MILLISECONDS)

    init {
        player.teleportAsync(location)
    }

    override fun close() {
        model.close()

        player.teleportAsync(lastLocation)

        cameraEntity.detachPlayer()
        cameraEntity.remove()

        tickTask.cancel()
    }

    private fun cameraLocation(): Location = location.clone().apply {
        val position = camera.hitBoxPosition()
        val rotation = camera.worldRotation()

        add(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
        yaw -= rotation.y
        pitch = rotation.x
    }

    override fun getLocation(): Location = location
    override fun getTracker(): Tracker = model
    override fun getCamera(): RenderedBone = camera
    override fun getCameraEntity(): CameraEntity = cameraEntity
}