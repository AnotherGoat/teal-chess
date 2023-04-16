/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Color;

/** The rook piece. It can move horizontally and vertically. */
public final class Rook extends SlidingPiece {

    private static final List<int[]> MOVES =
            List.of(new int[] {0, 1}, new int[] {-1, 0}, new int[] {1, 0}, new int[] {0, -1});

    public Rook(String position, Color color) {
        this(position, color, true);
    }

    @Override
    public Rook moveTo(String destination) {
        return new Rook(destination, color, false);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♖" : "♜";
    }

    private Rook(String position, Color color, boolean firstMove) {
        super(PieceType.ROOK, position, color, firstMove, MOVES);
    }
}
