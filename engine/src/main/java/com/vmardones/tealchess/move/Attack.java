/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import com.vmardones.tealchess.board.Square;
import com.vmardones.tealchess.piece.Piece;

/**
 * A potential attacking move, even if making it would leave the king of the same color in check. This is useful for checking checks, checkmates and stalemates, because a king cannot move to an attacked tile.
 * @param piece The attacking piece.
 * @param target Attacked square.
 * @see <a href="https://www.chessprogramming.org/Attacks">Attacks</a>
 */
public record Attack(Piece piece, Square target) {
    public Attack {
        if (piece.coordinate().equals(target.coordinate())) {
            throw new IllegalMoveException("The source and destination squares cannot be the same");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(piece.coordinate()) + target.coordinate();
    }
}
