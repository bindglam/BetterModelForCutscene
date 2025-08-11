package com.bindglam.bm4cutscene.cutscene;

import kr.toxicity.model.api.tracker.TrackerModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record CutsceneProperties(String modelId, String animation, boolean shiftToClose, TrackerModifier modifier) {
    private static final TrackerModifier DEFAULT_MODIFIER = TrackerModifier.builder().sightTrace(false).damageAnimation(false).damageTint(false).shadow(false).build();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String modelId;
        private String animation;
        private boolean shiftToClose = true;
        private TrackerModifier modifier = DEFAULT_MODIFIER;

        private Builder() {
        }

        public Builder model(@NotNull String id) {
            this.modelId = id;
            return this;
        }

        public Builder animation(@NotNull String id) {
            this.animation = id;
            return this;
        }

        public Builder shiftToClose(boolean shiftToClose) {
            this.shiftToClose = shiftToClose;
            return this;
        }

        public Builder trackerModifier(@NotNull TrackerModifier modifier) {
            this.modifier = modifier;
            return this;
        }

        public CutsceneProperties build() {
            Objects.requireNonNull(modelId);
            Objects.requireNonNull(animation);
            Objects.requireNonNull(modifier);

            return new CutsceneProperties(modelId, animation, shiftToClose, modifier);
        }
    }
}
