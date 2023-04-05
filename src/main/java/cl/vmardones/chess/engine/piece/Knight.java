/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Alliance;

/** The knight piece. It moves in an L shape. */
public final class Knight extends JumpingPiece {

    public Knight(Coordinate position, Alliance alliance) {
        this(position, alliance, true);
    }

    @Override
    public String singleChar() {
        return alliance == Alliance.BLACK ? "n" : "N";
    }

    @Override
    public Knight moveTo(Coordinate destination) {
        return new Knight(destination, alliance, false);
    }

    private Knight(Coordinate position, Alliance alliance, boolean firstMove) {
        super(position, alliance, firstMove, KNIGHT_JUMPS);
    }
}
