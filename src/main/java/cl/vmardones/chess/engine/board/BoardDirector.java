/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import cl.vmardones.chess.engine.game.Position;

/** Provides utility functions to create chessboards quickly. */
public final class BoardDirector {

    /* Board creation */

    /**
     * Creates a standard chessboard, which consists of a rank filled with 8 pawns on each side with a
     * formation of 8 major pieces behind.
     *
     * @return The standard chessboard.
     */
    public static Board createStandardBoard() {
        return Position.INITIAL_POSITION.board();
    }

    @ExcludeFromGeneratedReport
    private BoardDirector() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
