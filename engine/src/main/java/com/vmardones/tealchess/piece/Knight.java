/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;


public final class Knight extends Piece {

    private static final List<Vector> MOVES = List.of(
            new Vector(-1, 2),
            new Vector(1, 2),
            new Vector(-2, 1),
            new Vector(2, 1),
            new Vector(-2, -1),
            new Vector(2, -1),
            new Vector(-1, -2),
            new Vector(1, -2));

    public Knight(Coordinate coordinate, Color color) {
        super(PieceType.KNIGHT, coordinate, color, MOVES, false);
    }

    @Override
    public Knight moveTo(Coordinate destination) {
        return new Knight(destination, color);
    }

}
