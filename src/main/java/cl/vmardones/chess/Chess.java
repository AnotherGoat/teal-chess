/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess;

import cl.vmardones.chess.cli.MainCommand;
import picocli.CommandLine;

/** The chess game launcher. */
public final class Chess {

    /**
     * Launch the game.
     *
     * @param args Command line arguments.
     */
    public static void main(String... args) {
        new CommandLine(new MainCommand()).execute(args);
    }

    @ExcludeFromGeneratedReport
    private Chess() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
