/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.move.LegalMove;
import org.eclipse.jdt.annotation.Nullable;

final class GameState {

    private Position position;
    private @Nullable LegalMove lastMove;

    GameState() {
        position = Position.INITIAL_POSITION;
    }

    GameMemento save() {
        return new GameMemento(position, lastMove);
    }

    void load(GameMemento gameMemento) {
        position = gameMemento.position();
        lastMove = gameMemento.lastMove();
    }

    Position position() {
        return position;
    }

    void position(Position value) {
        position = value;
    }

    @Nullable LegalMove lastMove() {
        return lastMove;
    }

    void lastMove(LegalMove value) {
        lastMove = value;
    }
}
