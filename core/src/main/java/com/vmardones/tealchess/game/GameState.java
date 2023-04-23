/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

// TODO: See if this class can be immutable
final class GameState {

    private Position currentPosition;

    GameState() {
        currentPosition = Position.INITIAL_POSITION;
    }

    PositionMemento save() {
        return new PositionMemento(currentPosition);
    }

    void load(PositionMemento positionMemento) {
        currentPosition = positionMemento.state();
    }

    Position currentPosition() {
        return currentPosition;
    }

    void currentPosition(Position value) {
        currentPosition = value;
    }
}
