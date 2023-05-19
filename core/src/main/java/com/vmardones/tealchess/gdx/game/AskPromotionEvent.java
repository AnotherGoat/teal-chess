/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.move.Move;

final class AskPromotionEvent extends Event {

    private final ClickableSquare square;
    private final List<Move> promotionMoves;

    AskPromotionEvent(ClickableSquare square, List<Move> promotionMoves) {
        this.square = square;
        this.promotionMoves = promotionMoves;
    }

    /* Getters */

    public ClickableSquare square() {
        return square;
    }

    public List<Move> promotionMoves() {
        return promotionMoves;
    }
}
