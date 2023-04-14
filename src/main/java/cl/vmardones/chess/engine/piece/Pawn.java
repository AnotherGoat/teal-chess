/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Alliance;

/**
 * The pawn piece. It only moves forward (depending on the side) and can eat other pieces
 * diagonally. A very weak piece, but it can be promoted when getting to the last rank at the
 * opposite side.
 */
public final class Pawn extends JumpingPiece {

    public Pawn(String position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public Pawn moveTo(String destination) {
        return new Pawn(destination, alliance, false);
    }

    @Override
    public String unicodeChar() {
        return alliance == Alliance.WHITE ? "♙" : "♟";
    }

    private Pawn(String position, Alliance alliance, boolean firstMove) {
        super(position, alliance, firstMove, List.of(new int[] {0, alliance.direction()}));
    }
}
