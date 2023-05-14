/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.position;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.color.Color;

public record Position(Board board, Color sideToMove, int halfmoveClock, int fullmoveCounter) {

    /**
     * The initial position of a chess game.
     * The board is in its initial state, side to move is white, castling rights are full, no en passant target is set, halfmove clock starts at 0 and fullmove counter starts at 1.
     * @see <a href="https://www.chessprogramming.org/Initial_Position">Initial Position</a>
     */
    public static final Position INITIAL_POSITION = createInitialPosition();

    private static Position createInitialPosition() {
        return new Position(Board.INITIAL_BOARD, Color.WHITE, 0, 1);
    }
}
