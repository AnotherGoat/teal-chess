/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;

public final class Queen extends Piece {

    private static final List<Vector> MOVES = List.of(
            new Vector(-1, 1),
            new Vector(0, 1),
            new Vector(1, 1),
            new Vector(-1, 0),
            new Vector(1, 0),
            new Vector(-1, -1),
            new Vector(0, -1),
            new Vector(1, -1));

    public Queen(Coordinate coordinate, Color color) {
        super(PieceType.QUEEN, coordinate, color, MOVES, true);
    }

    @Override
    public Queen moveTo(Coordinate destination) {
        return new Queen(destination, color);
    }

    @Override
    public String unicode() {
        return color.isWhite() ? "♕" : "♛";
    }
}
