/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Color;

/** The bishop piece. It can move diagonally. */
public final class Bishop extends SlidingPiece {

    private static final List<int[]> MOVES =
            List.of(new int[] {-1, 1}, new int[] {1, 1}, new int[] {-1, -1}, new int[] {1, -1});

    public Bishop(String position, Color color) {
        this(position, color, true);
    }

    @Override
    public Bishop moveTo(String destination) {
        return new Bishop(destination, color, false);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♗" : "♝";
    }

    private Bishop(String position, Color color, boolean firstMove) {
        super(PieceType.BISHOP, position, color, firstMove, MOVES);
    }
}
