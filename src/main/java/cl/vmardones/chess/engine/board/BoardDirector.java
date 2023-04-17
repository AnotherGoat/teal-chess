/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.parser.FenParser;

/** Provides utility functions to create chessboards quickly. */
public final class BoardDirector {

    private static final Board STANDARD_BOARD = generateStandardBoard();

    /* Board creation */

    /**
     * Creates a standard chessboard, which consists of a rank filled with 8 pawns on each side with a
     * formation of 8 major pieces behind.
     *
     * @return The standard chessboard.
     */
    public static Board createStandardBoard() {
        return STANDARD_BOARD;
    }

    @ExcludeFromGeneratedReport
    private BoardDirector() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }

    private static Board generateStandardBoard() {
        return FenParser.parse("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                .board();
    }
}
