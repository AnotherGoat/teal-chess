/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;

public final class Bishop extends Piece {

    private static final List<Vector> MOVES =
            List.of(new Vector(-1, 1), new Vector(1, 1), new Vector(-1, -1), new Vector(1, -1));

    public Bishop(Coordinate coordinate, Color color) {
        super(PieceType.BISHOP, coordinate, color, MOVES, true);
    }

    @Override
    public Bishop moveTo(Coordinate destination) {
        return new Bishop(destination, color);
    }

    @Override
    public String unicode() {
        return color.isWhite() ? "♗" : "♝";
    }
}
