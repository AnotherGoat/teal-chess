/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    public static PieceType fromSymbol(String symbol) {
        return switch (symbol) {
            case "P", "p" -> PAWN;
            case "N", "n" -> KNIGHT;
            case "B", "b" -> BISHOP;
            case "R", "r" -> ROOK;
            case "Q", "q" -> QUEEN;
            case "K", "k" -> KING;
            default -> throw new PieceSymbolException(symbol);
        };
    }
}
