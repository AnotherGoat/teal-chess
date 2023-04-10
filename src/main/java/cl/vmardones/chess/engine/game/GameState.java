/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

// TODO: See if this class can be immutable

import org.eclipse.jdt.annotation.Nullable;

/** A mutable class that holds the current state of the game. */
final class GameState {

    private @Nullable Turn currentTurn;

    TurnMemento save() {
        if (currentTurn == null) {
            throw new IllegalSaveException();
        }

        return new TurnMemento(currentTurn);
    }

    void load(TurnMemento turnMemento) {
        currentTurn = turnMemento.state();
    }

    @Nullable Turn currentTurn() {
        return currentTurn;
    }

    void currentTurn(Turn value) {
        currentTurn = value;
    }
}
