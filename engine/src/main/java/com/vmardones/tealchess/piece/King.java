/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;

public final class King extends Piece {

    private static final List<Vector> MOVES = List.of(
            new Vector(-1, 1),
            new Vector(0, 1),
            new Vector(1, 1),
            new Vector(-1, 0),
            new Vector(1, 0),
            new Vector(-1, -1),
            new Vector(0, -1),
            new Vector(1, -1));

    public King(Coordinate coordinate, Color color) {
        super(PieceType.KING, coordinate, color, MOVES, false);
    }

    @Override
    public King moveTo(Coordinate destination) {
        return new King(destination, color);
    }
}
