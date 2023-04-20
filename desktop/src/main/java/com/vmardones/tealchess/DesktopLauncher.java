/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess;

import com.vmardones.tealchess.cli.MainCommand;
import picocli.CommandLine;

/** The chess game launcher. */
public final class DesktopLauncher {

    /**
     * Launch the game.
     *
     * @param args Command line arguments.
     */
    public static void main(String... args) {
        new CommandLine(new MainCommand()).execute(args);
    }

    @ExcludeFromGeneratedReport
    private DesktopLauncher() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
