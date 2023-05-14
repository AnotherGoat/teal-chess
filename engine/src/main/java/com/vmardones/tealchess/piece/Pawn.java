/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.player.Color;


public final class Pawn extends Piece {

    public Pawn(Coordinate coordinate, Color color) {
        super(PieceType.PAWN, coordinate, color, color.isWhite() ? WHITE_MOVES : BLACK_MOVES, false);
    }

    @Override
    public Pawn moveTo(Coordinate destination) {
        return new Pawn(destination, color);
    }

    public boolean canDoublePush() {
        return coordinate.rank() == doublePushRank();
    }

    public boolean canBePromoted() {
        return coordinate.rank() == promotionRank();
    }

    /**
     * Promote this pawn to a knight, a bishop, a rook or a queen.
     * No checks of any kind are made in this method.
     * @param choice The piece that this pawn will be promoted to.
     * @return The promoted piece.
     */
    public Piece promote(PromotionChoice choice) {
        return switch (choice) {
            case KNIGHT -> new Knight(coordinate, color);
            case BISHOP -> new Bishop(coordinate, color);
            case ROOK -> new Rook(coordinate, color);
            case QUEEN -> new Queen(coordinate, color);
        };
    }

    private int doublePushRank() {
        return color.isWhite() ? 2 : 7;
    }

    private int promotionRank() {
        return color.isWhite() ? 7 : 2;
    }
}
