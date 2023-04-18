/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;

import cl.vmardones.chess.engine.player.Color;

/**
 * The pawn piece. It only moves forward (depending on the side) and can eat other pieces
 * diagonally. A very weak piece, but it can be promoted when getting to the last rank at the
 * opposite side.
 * @see <a href="https://www.chessprogramming.org/Pawn">Pawn</a>
 */
public final class Pawn extends JumpingPiece {

    public Pawn(String coordinate, Color color) {
        super(PieceType.PAWN, coordinate, color, List.of(new int[] {0, color.direction()}));
    }

    @Override
    public Pawn moveTo(String destination) {
        return new Pawn(destination, color);
    }

    @Override
    public String unicodeChar() {
        return color == Color.WHITE ? "♙" : "♟";
    }

    public Piece promote() {
        return new Queen(coordinate.toString(), color);
    }
}
