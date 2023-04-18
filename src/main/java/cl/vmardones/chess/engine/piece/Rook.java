/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Color;

/**
 * The rook piece. It can move horizontally and vertically.
 * @see <a href="https://www.chessprogramming.org/Rook">Rook</a>
 */
public final class Rook extends SlidingPiece {

    public static final Coordinate WHITE_KING_SIDE = Coordinate.of("h1");
    public static final Coordinate WHITE_QUEEN_SIDE = Coordinate.of("a1");
    public static final Coordinate BLACK_KING_SIDE = Coordinate.of("h8");
    public static final Coordinate BLACK_QUEEN_SIDE = Coordinate.of("a8");

    private static final List<int[]> MOVES =
            List.of(new int[] {0, 1}, new int[] {-1, 0}, new int[] {1, 0}, new int[] {0, -1});

    public Rook(String coordinate, Color color) {
        super(PieceType.ROOK, coordinate, color, MOVES);
    }

    @Override
    public Rook moveTo(String destination) {
        return new Rook(destination, color);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♖" : "♜";
    }
}
