/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.settings;

import java.util.Locale;

public enum StringSetting {
    PGN(""),
    FEN("");

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
