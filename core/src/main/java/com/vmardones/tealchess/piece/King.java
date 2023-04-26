/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.player.Color;

/**
 * The king piece. The most important piece in the game, must be defended at all costs. It moves
 * like the queen, but only one space at a time. It also cannot move into a coordinate where it could
 * be captured.
 * @see <a href="https://www.chessprogramming.org/King">King</a>
 */
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

    public King(String coordinate, Color color) {
        super(PieceType.KING, coordinate, color, MOVES, false);
    }

    @Override
    public King moveTo(String destination) {
        return new King(destination, color);
    }

    @Override
    public String unicode() {
        return color.isWhite() ? "♔" : "♚";
    }
}
