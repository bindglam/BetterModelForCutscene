package com.bindglam.bm4cutscene.cutscene

import com.bindglam.bm4cutscene.nms.CameraEntityImpl
import io.papermc.paper.entity.LookAnchor
import io.papermc.paper.math.Position
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kr.toxicity.model.api.BetterModel
import kr.toxicity.model.api.animation.AnimationModifier
import kr.toxicity.model.api.bone.RenderedBone
import kr.toxicity.model.api.tracker.EntityTracker
import kr.toxicity.model.api.tracker.TrackerModifier
import kr.toxicity.model.api.tracker.TrackerUpdateAction
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

class CutsceneImpl(private val plugin: Plugin, private val player: Player, private val location: Location, private val modelId: String, private val animation: String) : Cutscene {
    private val entity = location.world.spawn(location, ArmorStand::class.java).apply {
        setAI(false)
        setGravity(false)
        isSmall = true
    }

    private val model = BetterModel.model(modelId)
        .map { r ->
            r.create(entity, TrackerModifier.DEFAULT) { tracker ->
                tracker.update(TrackerUpdateAction.brightness(15, 15))
                tracker.animate(animation, AnimationModifier.DEFAULT_WITH_PLAY_ONCE) {
                    tracker.close()

                    cameraEntity.detachPlayer()
                    cameraEntity.remove()
                    Bukkit.getScheduler().runTask(plugin) { task ->
                        entity.remove()
                    }

                    tickTask.cancel()
                }
                tracker.markPlayerForSpawn(player)
            }
        }
        .get()

    private val camera = model.bone("camera")!!

    private val cameraEntity: CameraEntity = CameraEntityImpl(player, cameraLocation()).apply { spawn() }

    val tickTask: ScheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { task ->
        cameraEntity.moveDuration(camera.interpolationDuration())
        cameraEntity.location = cameraLocation()
        cameraEntity.update()

        cameraEntity.attachPlayer()
    }, 50L, 50L, TimeUnit.MILLISECONDS)

    init {
        player.lookAt(Position.fine(location), LookAnchor.FEET)
    }

    fun cameraLocation(): Location = location.clone().apply {
        val position = camera.hitBoxPosition()
        val rotation = camera.worldRotation()

        add(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
        yaw -= rotation.y
        pitch = rotation.x
    }

    override fun getLocation(): Location = location
    override fun getTracker(): EntityTracker = model
    override fun getCamera(): RenderedBone = camera
    override fun getCameraEntity(): CameraEntity = cameraEntity
}