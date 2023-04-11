/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Alliance;

/** The rook piece. It can move horizontally and vertically. */
public final class Rook extends SlidingPiece {

    private static final List<int[]> MOVES =
            List.of(new int[] {0, 1}, new int[] {-1, 0}, new int[] {1, 0}, new int[] {0, -1});

    public Rook(String position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public Rook moveTo(String destination) {
        return new Rook(destination, alliance, false);
    }

    private Rook(String position, Alliance alliance, boolean firstMove) {
        super(position, alliance, firstMove, MOVES);
    }
}
