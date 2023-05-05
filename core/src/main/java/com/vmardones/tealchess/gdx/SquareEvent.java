/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.board.Square;
import org.eclipse.jdt.annotation.Nullable;

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

    @Nullable Sprite sprite() {
        return square.sprite();
    }

    void removeSprite() {
        square.removeSprite();
    }
}
