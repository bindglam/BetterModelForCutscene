package com.bindglam.bm4cutscene.cutscene;

import java.util.Objects;

public record CutsceneProperties(String modelId, String animation, boolean shiftToClose) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String modelId;
        private String animation;
        private boolean shiftToClose = true;

        private Builder() {
        }

        public Builder model(String id) {
            this.modelId = id;
            return this;
        }

        public Builder animation(String id) {
            this.animation = id;
            return this;
        }

        public Builder shiftToClose(boolean shiftToClose) {
            this.shiftToClose = shiftToClose;
            return this;
        }

        public CutsceneProperties build() {
            Objects.requireNonNull(modelId);
            Objects.requireNonNull(animation);

            return new CutsceneProperties(modelId, animation, shiftToClose);
        }
    }
}
