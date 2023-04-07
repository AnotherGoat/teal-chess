/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;
import java.util.stream.Stream;

import cl.vmardones.chess.engine.player.Alliance;

/**
 * The queen, the strongest piece in the game. It can move horizontally, vertically and diagonally.
 */
public final class Queen extends SlidingPiece {

    public Queen(String position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public Queen moveTo(String destination) {
        return new Queen(destination, alliance, false);
    }

    private Queen(String position, Alliance alliance, boolean firstMove) {
        super(position, alliance, firstMove, generateMoveVectors());
    }

    private static List<int[]> generateMoveVectors() {
        return Stream.concat(ORTHOGONALS.stream(), DIAGONALS.stream()).toList();
    }
}
