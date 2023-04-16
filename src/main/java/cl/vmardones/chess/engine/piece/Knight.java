/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Color;

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
        this(coordinate, color, true);
    }

    @Override
    public String singleChar() {
        return color == Color.BLACK ? "n" : "N";
    }

    @Override
    public Knight moveTo(String destination) {
        return new Knight(destination, color, false);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♘" : "♞";
    }

    private Knight(String coordinate, Color color, boolean firstMove) {
        super(PieceType.KNIGHT, coordinate, color, firstMove, MOVES);
    }
}
