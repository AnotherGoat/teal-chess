/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.player.Color;

/**
 * The queen, the strongest piece in the game. It can move horizontally, vertically and diagonally.
 * @see <a href="https://www.chessprogramming.org/Queen">Queen</a>
 */
public final class Queen extends SlidingPiece {

    private static final List<Vector> MOVES = List.of(
            new Vector(-1, 1),
            new Vector(0, 1),
            new Vector(1, 1),
            new Vector(-1, 0),
            new Vector(1, 0),
            new Vector(-1, -1),
            new Vector(0, -1),
            new Vector(1, -1));

    public Queen(String coordinate, Color color) {
        super(PieceType.QUEEN, coordinate, color, MOVES);
    }

    @Override
    public Queen moveTo(String destination) {
        return new Queen(destination, color);
    }

    @Override
    public String unicode() {
        return color.isWhite() ? "♕" : "♛";
    }
}
