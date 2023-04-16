/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

// TODO: See if this class can be immutable

import org.eclipse.jdt.annotation.Nullable;

/** A mutable class that holds the current state of the game. */
final class GameState {

    private @Nullable Position currentPosition;

    PositionMemento save() {
        if (currentPosition == null) {
            throw new IllegalSaveException();
        }

        return new PositionMemento(currentPosition);
    }

    void load(PositionMemento positionMemento) {
        currentPosition = positionMemento.state();
    }

    @Nullable Position currentPosition() {
        return currentPosition;
    }

    void currentPosition(Position value) {
        currentPosition = value;
    }
}
