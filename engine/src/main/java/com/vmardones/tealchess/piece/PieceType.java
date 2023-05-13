/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

enum PieceType {
    PAWN("P", 1),
    KNIGHT("N", 3),
    BISHOP("B", 3),
    ROOK("R", 5),
    QUEEN("Q", 9),
    KING("K", 200);

    private final String firstChar;
    private final int value;

    String firstChar() {
        return firstChar;
    }

    int value() {
        return value;
    }

    PieceType(String firstChar, int value) {
        this.firstChar = firstChar;
        this.value = value;
    }
}
