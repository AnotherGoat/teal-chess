/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Color;

/**
 * The queen, the strongest piece in the game. It can move horizontally, vertically and diagonally.
 */
public final class Queen extends SlidingPiece {

    private static final List<int[]> MOVES = List.of(
            new int[] {-1, 1},
            new int[] {0, 1},
            new int[] {1, 1},
            new int[] {-1, 0},
            new int[] {1, 0},
            new int[] {-1, -1},
            new int[] {0, -1},
            new int[] {1, -1});

    public Queen(String position, Color color) {
        this(position, color, true);
    }

    @Override
    public Queen moveTo(String destination) {
        return new Queen(destination, color, false);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♕" : "♛";
    }

    private Queen(String position, Color color, boolean firstMove) {
        super(position, color, firstMove, MOVES);
    }
}
