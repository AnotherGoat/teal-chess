/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Color;

/**
 * The king piece. The most important piece in the game, must be defended at all costs. It moves
 * like the queen, but only one space at a time. It also cannot move into a position where it could
 * be captured.
 */
public final class King extends JumpingPiece {

    private static final List<int[]> MOVES = List.of(
            new int[] {-1, 1},
            new int[] {0, 1},
            new int[] {1, 1},
            new int[] {-1, 0},
            new int[] {1, 0},
            new int[] {-1, -1},
            new int[] {0, -1},
            new int[] {1, -1});

    public King(String position, Color color) {
        this(position, color, true);
    }

    @Override
    public King moveTo(String destination) {
        return new King(destination, color, false);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♔" : "♚";
    }

    private King(String position, Color color, boolean firstMove) {
        super(position, color, firstMove, MOVES);
    }
}
