/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.move.Move;

final class PromotionEvent extends Event {

    private final Move move;

    PromotionEvent(Move move) {
        this.move = move;
    }

    /* Getters */

    public Move move() {
        return move;
    }
}
