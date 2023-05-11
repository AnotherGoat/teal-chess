/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.assets;

import java.util.Locale;

public enum PieceTheme {
    CBURNETT;

    public static PieceTheme of(String name) {
        return valueOf(name.toUpperCase(Locale.ROOT));
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ROOT);
    }
}
