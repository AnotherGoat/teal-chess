/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

import java.util.Arrays;

import com.vmardones.tealchess.ExcludeFromGeneratedReport;
import com.vmardones.tealchess.game.Game;

/**
 * PGN (Portable Game Notation) parser.
 * Parses data written in PNG import format.
 */
public final class PgnParser {

    public static Game parse(String pgn) {
        var lines = Arrays.asList(pgn.split("\n", -1));

        var tags = lines.subList(0, lines.size() - 1);
        var moveText = lines.get(lines.size() - 1);

        var tagMap = PgnTagParser.parseTags(tags);

        return new Game();
    }

    @ExcludeFromGeneratedReport
    private PgnParser() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
