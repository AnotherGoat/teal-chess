/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

final class SquareEvent extends Event {

    private final Square square;

    SquareEvent(Square square) {
        this.square = square;
    }

    /* Getters */

    Coordinate coordinate() {
        return square.coordinate();
    }

    @Nullable Piece piece() {
        return square.piece();
    }

    float x() {
        return square.getX();
    }

    float y() {
        return square.getY();
    }
}
