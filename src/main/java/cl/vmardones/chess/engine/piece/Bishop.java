/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Alliance;

/** The bishop piece. It can move diagonally. */
public final class Bishop extends SlidingPiece {

    private static final List<int[]> MOVES =
            List.of(new int[] {-1, 1}, new int[] {1, 1}, new int[] {-1, -1}, new int[] {1, -1});

    public Bishop(String position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public Bishop moveTo(String destination) {
        return new Bishop(destination, alliance, false);
    }

    @Override
    public String unicodeChar() {
        return alliance == Alliance.WHITE ? "♗" : "♝";
    }

    private Bishop(String position, Alliance alliance, boolean firstMove) {
        super(position, alliance, firstMove, MOVES);
    }
}
