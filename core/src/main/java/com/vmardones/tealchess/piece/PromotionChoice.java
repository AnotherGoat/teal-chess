/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

/**
 * Each of the choices that a pawn can be promoted to when it reaches the opposite side.
 */
public enum PromotionChoice {
    KNIGHT("=N"),
    BISHOP("=B"),
    ROOK("=R"),
    QUEEN("=Q");

    private final String san;

    @Override
    public String toString() {
        return san;
    }

    PromotionChoice(String san) {
        this.san = san;
    }
}
