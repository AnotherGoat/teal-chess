/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import com.vmardones.tealchess.parser.pgn.San;

/**
 * Each of the choices that a pawn can be promoted to when it reaches the opposite side.
 */
public enum PromotionChoice implements San {
    QUEEN(PieceType.QUEEN),
    KNIGHT(PieceType.KNIGHT),
    ROOK(PieceType.ROOK),
    BISHOP(PieceType.BISHOP);

    private final PieceType type;

    public static PromotionChoice fromSymbol(String symbol) {
        return switch (symbol) {
            case "q" -> PromotionChoice.QUEEN;
            case "n" -> PromotionChoice.KNIGHT;
            case "r" -> PromotionChoice.ROOK;
            case "b" -> PromotionChoice.BISHOP;
                // TODO: Throw an exception instead
            default -> PromotionChoice.QUEEN;
        };
    }

    /* Getters */

    @Override
    public String san() {
        return type.whiteFen();
    }

    public PieceType type() {
        return type;
    }

    PromotionChoice(PieceType type) {
        this.type = type;
    }
}
