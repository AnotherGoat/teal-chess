/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.piece.Piece;

/**
 * A potential attacking move, even if making it would leave the king of the same color in check. This is useful for checking checks, checkmates and stalemates, because a king cannot move to an attacked tile.
 * @param piece The attacking piece.
 * @param target Attacked coordinate.
 * @see <a href="https://www.chessprogramming.org/Attacks">Attacks</a>
 */
public record Attack(Piece piece, Coordinate target) {
    public Attack {
        if (piece.coordinate().equals(target)) {
            throw new IllegalMoveException("The source and destination cannot be the same coordinate");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(piece.coordinate()) + target;
    }
}
