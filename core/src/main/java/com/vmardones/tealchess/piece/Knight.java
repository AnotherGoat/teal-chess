/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.player.Color;

/**
 * The knight piece. It moves in an L shape.
 * @see <a href="https://www.chessprogramming.org/Knight">Knight</a>
 */
public final class Knight extends JumpingPiece {

    private static final List<int[]> MOVES = List.of(
            new int[] {-1, 2},
            new int[] {1, 2},
            new int[] {-2, 1},
            new int[] {2, 1},
            new int[] {-2, -1},
            new int[] {2, -1},
            new int[] {-1, -2},
            new int[] {1, -2});

    public Knight(String coordinate, Color color) {
        super(PieceType.KNIGHT, coordinate, color, MOVES);
    }

    @Override
    public Knight moveTo(String destination) {
        return new Knight(destination, color);
    }

    @Override
    public String unicode() {
        return color.isWhite() ? "♘" : "♞";
    }
}
