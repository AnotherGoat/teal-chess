/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.board.Square;

final class SquareEvent extends Event {

    private final ClickableSquare square;

    SquareEvent(ClickableSquare square) {
        this.square = square;
    }

    /* Getters */

    Square square() {
        return square.square();
    }

    float x() {
        return square.getX();
    }

    float y() {
        return square.getY();
    }
}
