package com.bindglam.bm4cutscene.cutscene

import com.bindglam.bm4cutscene.nms.CameraEntity
import com.bindglam.bm4cutscene.nms.CameraEntityImpl
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kr.toxicity.model.api.BetterModel
import kr.toxicity.model.api.animation.AnimationModifier
import kr.toxicity.model.api.bone.RenderedBone
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
    companion object {
        private val MODIFIER = TrackerModifier.builder().sightTrace(false).damageAnimation(false).damageTint(false).shadow(false).build()
    }

    private val lastLocation = player.location

    private val model = BetterModel.model(properties.modelId)
        .map { r ->
            r.create(location, MODIFIER) { tracker ->
                tracker.update(TrackerUpdateAction.brightness(15, 15))
                tracker.animate(properties.animation, AnimationModifier.DEFAULT_WITH_PLAY_ONCE) {
                    if(!properties.shiftToClose)
                        close()
                }
                tracker.spawn(player)
            }
        }
        .get()
    private val camera = model.bone("camera")!!
    private val cameraEntity: CameraEntity = CameraEntityImpl(player, cameraLocation()).apply { spawn() }

    private val tickTask: ScheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { task ->
        if(model.isRunningSingleAnimation) {
            cameraEntity.moveDuration(camera.interpolationDuration())
            cameraEntity.location = cameraLocation()
            cameraEntity.update()

            cameraEntity.attachPlayer()
        }

        if(!model.isRunningSingleAnimation && properties.shiftToClose) {
            player.sendActionBar(Component.keybind("key.sneak").append(Component.text("로 나가기")))

            if(player.isSneaking)
                close()
        }
    }, 50L, 50L, TimeUnit.MILLISECONDS)

    init {
        player.teleportAsync(location)
    }

    fun cameraLocation(): Location = location.clone().apply {
        val position = camera.hitBoxPosition()
        val rotation = camera.worldRotation()

        add(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
        yaw -= rotation.y
        pitch = rotation.x
    }

    override fun close() {
        model.close()

        player.teleportAsync(lastLocation)

        cameraEntity.detachPlayer()
        cameraEntity.remove()

        tickTask.cancel()
    }

    override fun getLocation(): Location = location
    override fun getTracker(): Tracker = model
    override fun getCamera(): RenderedBone = camera
    override fun getCameraEntity(): CameraEntity = cameraEntity
}