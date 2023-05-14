/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.Locale;

import com.vmardones.tealchess.color.Color;

public record Piece(PieceType type, Color color, int coordinate) {

    /* Static factory methods */

    /**
     * Alternative method to build a piece, useful for parsing.
     * Only valid symbols are "PNBRQK" for white pieces and "pnbrqk" for black pieces;
     * The color is inferred from the symbol being uppercase (white) or lowercase (black).
     *
     * @param symbol The piece symbol.
     * @param coordinate The coordinate to put the piece in.
     * @return The piece with the asked symbol.
     */
    public static Piece fromSymbol(String symbol, int coordinate) {
        var color = Character.isUpperCase(symbol.charAt(0)) ? Color.WHITE : Color.BLACK;
        var upperCaseSymbol = symbol.toUpperCase(Locale.ROOT);

        return new Piece(PieceType.fromSymbol(symbol), color, coordinate);
    }

    /* Checking piece types */

    public boolean isPawn() {
        return type == PieceType.PAWN;
    }

    public boolean isKnight() {
        return type == PieceType.KNIGHT;
    }

    public boolean isBishop() {
        return type == PieceType.BISHOP;
    }

    public boolean isRook() {
        return type == PieceType.ROOK;
    }

    public boolean isQueen() {
        return type == PieceType.QUEEN;
    }

    public boolean isKing() {
        return type == PieceType.KING;
    }
}
