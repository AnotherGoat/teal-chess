/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.move.LegalMove;

final class AskPromotionEvent extends Event {

    private final Square square;
    private final List<LegalMove> promotionMoves;

    AskPromotionEvent(Square square, List<LegalMove> promotionMoves) {
        this.square = square;
        this.promotionMoves = promotionMoves;
    }

    /* Getters */

    public Square square() {
        return square;
    }

    public List<LegalMove> promotionMoves() {
        return promotionMoves;
    }
}
