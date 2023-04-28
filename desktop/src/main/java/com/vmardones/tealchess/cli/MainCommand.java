/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.cli;

import com.vmardones.tealchess.gdx.Window;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

// TODO: Add a command line option to choose between ASCII and Unicode chess pieces
@Command(name = "chess-game", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public final class MainCommand implements Runnable {

    @Option(
            names = {"--debug", "-D"},
            description = "Enable debug mode")
    private boolean debugMode;

    @Option(
            names = {"--highlight-legals", "-l"},
            description = "Highlight legal moves when selecting a piece.",
            negatable = true,
            defaultValue = "true")
    private boolean highlightLegals;

    @Option(
            names = {"--flip-board", "-f"},
            description = "Flip the board, white at the top and black at the bottom.")
    private boolean flipBoard;

    @Override
    public void run() {
        new Window(debugMode, highlightLegals, flipBoard);
    }
}
