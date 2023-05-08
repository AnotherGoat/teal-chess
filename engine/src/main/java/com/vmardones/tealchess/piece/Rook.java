/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;

/**
 * The rook piece. It can move horizontally and vertically.
 * @see <a href="https://www.chessprogramming.org/Rook">Rook</a>
 */
public final class Rook extends Piece {

    private static final List<Vector> MOVES =
            List.of(new Vector(0, 1), new Vector(-1, 0), new Vector(1, 0), new Vector(0, -1));

    public Rook(Coordinate coordinate, Color color) {
        super(PieceType.ROOK, coordinate, color, MOVES, true);
    }

    @Override
    public Rook moveTo(Coordinate destination) {
        return new Rook(destination, color);
    }

    @Override
    public String unicode() {
        return color.isWhite() ? "♖" : "♜";
    }
}
