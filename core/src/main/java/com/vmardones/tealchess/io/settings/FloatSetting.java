/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.settings;

import java.util.Locale;

enum FloatSetting {
    ANIMATION_DURATION(0.3f, 0.1f, 0.7f, 0.1f),
    AI_DELAY(0.7f, 0.1f, 1.5f, 0.1f);

    private final float defaultValue;
    private final float min;
    private final float max;
    private final float step;

    FloatSetting(float defaultValue, float min, float max, float step) {
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    /* Getters */
    String key() {
        return name().toLowerCase(Locale.ROOT);
    }

    float defaultValue() {
        return defaultValue;
    }

    public float step() {
        return step;
    }

    float clamp(float value) {
        return Math.min(Math.max(value, min), max);
    }
}
