/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.piece.PromotionChoice;

final class PromotionEvent extends Event {

    private final PromotionChoice promotionChoice;

    PromotionEvent(PromotionChoice promotionChoice) {
        this.promotionChoice = promotionChoice;
    }

    /* Getters */

    PromotionChoice promotionChoice() {
        return promotionChoice;
    }
}
