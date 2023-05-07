/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.pgn;

import java.util.Arrays;

import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.move.MoveMaker;

/**
 * PGN (Portable Game Notation) parser.
 * Parses data written in PNG import format.
 */
public final class PgnParser {

    public static Game parse(String pgn) {
        var lines = Arrays.asList(pgn.split("\n", -1));

        var tags = lines.subList(0, lines.size() - 1);
        var moveText = lines.get(lines.size() - 1);

        var tagMap = TagParser.parse(tags);

        return new Game(new MoveMaker());
    }

    private PgnParser() {}
}
