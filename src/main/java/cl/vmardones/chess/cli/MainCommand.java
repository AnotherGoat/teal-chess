/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.cli;

import cl.vmardones.chess.gui.Table;
import org.slf4j.simple.SimpleLogger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "chess-game",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class)
public class MainCommand implements Runnable {

  @Option(
      names = {"-D", "--debug"},
      description = "Enable debug mode.")
  private boolean debugMode;

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

    if (debugMode) {
      System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "Debug");
    }

    new Table(darkTheme, highlightLegals, flipBoard);
  }
}
