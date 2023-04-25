/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.player.Color;

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
    public String unicode() {
        return color.isWhite() ? "♙" : "♟";
    }

    public boolean canBePromoted() {
        return coordinate.rank() == color.opposite().pawnRank();
    }

    public Piece promote(PromotionChoice choice) {
        return switch (choice) {
            case KNIGHT -> new Knight(coordinate.toString(), color);
            case BISHOP -> new Bishop(coordinate.toString(), color);
            case ROOK -> new Rook(coordinate.toString(), color);
            case QUEEN -> new Queen(coordinate.toString(), color);
        };
    }
}
