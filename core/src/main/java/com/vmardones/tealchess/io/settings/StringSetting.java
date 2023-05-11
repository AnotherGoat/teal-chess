/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.settings;

import java.util.Locale;

enum StringSetting {
    PGN(""),
    FEN(""),
    COLOR_THEME("lichess_org"),
    PIECE_THEME("cburnett");

    private final String defaultValue;

    StringSetting(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /* Getters */
    String key() {
        return name().toLowerCase(Locale.ROOT);
    }

    String defaultValue() {
        return defaultValue;
    }
}
