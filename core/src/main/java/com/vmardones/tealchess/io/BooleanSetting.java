/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io;

import java.util.Locale;

enum BooleanSetting {
    DEBUG_MODE(false),
    HIGHTLIGHT_LEGALS(true),
    FLIP_BOARD(false);

    private final boolean defaultValue;

    BooleanSetting(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /* Getters */
    String key() {
        return name().toLowerCase(Locale.ROOT);
    }

    boolean defaultValue() {
        return defaultValue;
    }
}
