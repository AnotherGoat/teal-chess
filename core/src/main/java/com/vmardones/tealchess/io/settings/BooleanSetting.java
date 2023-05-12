/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.settings;

import java.util.Locale;

enum BooleanSetting {
    DEBUG_MODE(false),
    SHOW_LEGALS(true),
    FLIP_BOARD(false),
    SHOW_LAST_MOVE(true),
    SHOW_COORDINATES(true),
    SHOW_ALL_COORDINATES(false),
    SHOW_ATTACKED_PIECES(false),
    PIECE_SHADOWS(true),
    ANIMATE_PIECES(true),
    INVISIBLE_PIECES(false);

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
