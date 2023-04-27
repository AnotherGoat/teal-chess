/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import com.vmardones.tealchess.parser.San;

/**
 * Each of the choices that a pawn can be promoted to when it reaches the opposite side.
 */
public enum PromotionChoice implements San {
    KNIGHT("=N"),
    BISHOP("=B"),
    ROOK("=R"),
    QUEEN("=Q");

    private final String san;

    /* Getters */

    @Override
    public String san() {
        return san;
    }

    PromotionChoice(String san) {
        this.san = san;
    }
}
