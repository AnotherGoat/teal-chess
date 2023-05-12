/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.move.LegalMove;

final class MoveEvent extends Event {

    private final LegalMove move;

    MoveEvent(LegalMove move) {
        this.move = move;
    }

    /* Getters */

    public LegalMove move() {
        return move;
    }
}
