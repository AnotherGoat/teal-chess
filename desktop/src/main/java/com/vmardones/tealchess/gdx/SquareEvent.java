/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.board.Square;

final class SquareEvent extends Event {

    private final Square square;
    private final float x;
    private final float y;

    SquareEvent(Square square, float x, float y) {
        this.square = square;
        this.x = x;
        this.y = y;
    }

    /* Getters */

    Square square() {
        return square;
    }

    float x() {
        return x;
    }

    float y() {
        return y;
    }
}
