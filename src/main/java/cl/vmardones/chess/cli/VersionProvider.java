/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.cli;

import picocli.CommandLine;

class VersionProvider implements CommandLine.IVersionProvider {

  private static final String[] UNKNOWN_VERSION = {"UNKNOWN"};

  @Override
  public String[] getVersion() {

    final var implementationVersion = getClass().getPackage().getImplementationVersion();

    if (implementationVersion == null || implementationVersion.isBlank()) {
      return UNKNOWN_VERSION;
    }

    return new String[] {implementationVersion};
  }
}