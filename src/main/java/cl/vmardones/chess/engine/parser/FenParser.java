/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.parser;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.board.Board;

/**
 * FEN (Forsyth-Edwards Notation) string parser.
 */
public final class FenParser {

    public static Board parse(String fen) {

        if (!isAscii(fen)) {
            throw new FenParseException("FEN string is not ASCII: " + fen);
        }

        return null;
    }

    @ExcludeFromGeneratedReport
    private FenParser() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static boolean isAscii(String text) {
        return text.chars().allMatch(character -> character < 0x80);
    }
}
