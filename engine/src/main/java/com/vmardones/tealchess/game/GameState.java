/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.player.Player;
import com.vmardones.tealchess.position.Position;
import org.jspecify.annotations.Nullable;

// TODO: Add method to get last saved state
public final class GameState {

    private Position position;
    private Player whitePlayer;
    private Player blackPlayer;
    private @Nullable Move lastMove;

    GameState(Position position, Player whitePlayer, Player blackPlayer) {
        this.position = position;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    GameMemento save() {
        return new GameMemento(position, whitePlayer, blackPlayer, lastMove);
    }

    void load(GameMemento gameMemento) {
        position = gameMemento.position();
        lastMove = gameMemento.lastMove();
    }

    /* Getters and setters */

    Position position() {
        return position;
    }

    void position(Position value) {
        position = value;
    }

    Player whitePlayer() {
        return whitePlayer;
    }

    void whitePlayer(Player value) {
        whitePlayer = value;
    }

    Player blackPlayer() {
        return blackPlayer;
    }

    void blackPlayer(Player value) {
        blackPlayer = value;
    }

    void lastMove(Move value) {
        lastMove = value;
    }
}
