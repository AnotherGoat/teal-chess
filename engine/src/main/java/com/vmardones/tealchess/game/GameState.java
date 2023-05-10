/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.analysis.PositionAnalyzer;
import com.vmardones.tealchess.move.LegalMove;
import com.vmardones.tealchess.player.Player;
import org.eclipse.jdt.annotation.Nullable;

final class GameState {

    private Position position;
    private Player whitePlayer;
    private Player blackPlayer;
    private @Nullable LegalMove lastMove;

    GameState() {
        position = Position.INITIAL_POSITION;

        var positionAnalyzer = PositionAnalyzer.INITIAL_ANALYZER;
        whitePlayer = positionAnalyzer.whitePlayer();
        blackPlayer = positionAnalyzer.blackPlayer();
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

    void lastMove(LegalMove value) {
        lastMove = value;
    }
}
