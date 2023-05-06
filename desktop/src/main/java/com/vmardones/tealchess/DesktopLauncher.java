/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess;

import com.vmardones.tealchess.gdx.Window;

/** The chess game launcher. */
public final class DesktopLauncher {

    /**
     * Launch the desktop version of the game.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String... args) {
        new Window();
    }

    @ExcludeFromGeneratedReport
    private DesktopLauncher() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
