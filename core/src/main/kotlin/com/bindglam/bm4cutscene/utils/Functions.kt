package com.bindglam.bm4cutscene.utils

import kr.toxicity.model.api.animation.AnimationIterator
import kr.toxicity.model.api.tracker.Tracker

val Tracker.isRunningAnimation: Boolean
    get() {
        val runningAnimation = pipeline.runningAnimation()

        return runningAnimation != null && runningAnimation.type == AnimationIterator.Type.PLAY_ONCE
    }