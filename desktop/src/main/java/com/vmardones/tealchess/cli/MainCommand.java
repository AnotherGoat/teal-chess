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
            names = {"-d", "--dark-theme"},
            description = "Enable dark theme.")
    private boolean darkTheme;

    @Option(
            names = {"-l", "--highlight-legals"},
            description = "Highlight legal moves when selecting a piece.",
            negatable = true,
            defaultValue = "true")
    private boolean highlightLegals;

    @Option(
            names = {"-f", "--flip-board"},
            description = "Flip the board, white at the top and black at the bottom.")
    private boolean flipBoard;

    @Override
    public void run() {
        System.setProperty("awt.useSystemAAFontSettings", "on");

        new Window(darkTheme, highlightLegals, flipBoard);
    }
}
