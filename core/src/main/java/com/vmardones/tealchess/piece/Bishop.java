/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.player.Color;

/**
 * The bishop piece. It can move diagonally.
 * @see <a href="https://www.chessprogramming.org/Bishop">Bishop</a>
 */
public final class Bishop extends SlidingPiece {

    private static final List<int[]> MOVES =
            List.of(new int[] {-1, 1}, new int[] {1, 1}, new int[] {-1, -1}, new int[] {1, -1});

    public Bishop(String coordinate, Color color) {
        super(PieceType.BISHOP, coordinate, color, MOVES);
    }

    @Override
    public Bishop moveTo(String destination) {
        return new Bishop(destination, color);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♗" : "♝";
    }
}
