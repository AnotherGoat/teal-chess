/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx.game;

import com.badlogic.gdx.Gdx;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.settings.SettingManager;
import com.vmardones.tealchess.parser.pgn.PgnSerializer;
import com.vmardones.tealchess.player.Color;

final class GameLogger {

    private static final String LOG_TAG = "Game";
    private final SettingManager settings;

    public GameLogger(SettingManager settings) {
        this.settings = settings;
    }

    void log(Game game) {
        save(game);

        var board = game.board();
        Gdx.app.debug(LOG_TAG, "Current chessboard:\n" + board.unicode());

        Gdx.app.debug(LOG_TAG, "White king: " + board.king(Color.WHITE));
        Gdx.app.debug(LOG_TAG, "White pieces: " + board.pieces(Color.WHITE));
        Gdx.app.debug(LOG_TAG, "Black king: " + board.king(Color.BLACK));
        Gdx.app.debug(LOG_TAG, "Black pieces: " + board.pieces(Color.BLACK));

        Gdx.app.debug(LOG_TAG, "Castling rights: " + game.castlingRights().fen());

        var enPassantTarget = game.enPassantTarget();
        if (enPassantTarget != null) {
            Gdx.app.debug(LOG_TAG, "En passant target: " + board.unicodeSquare(enPassantTarget) + enPassantTarget);
        }

        Gdx.app.debug(LOG_TAG, "Players: " + game.playerInfo() + " vs. " + game.opponentInfo());

        var sideToMove = game.sideToMove();
        Gdx.app.log(LOG_TAG, sideToMove + "'s turn!");
        Gdx.app.debug(LOG_TAG, "Legal moves: " + game.legalMoves());

        var moves = game.moveHistory();
        Gdx.app.debug(LOG_TAG, "Move history: " + PgnSerializer.serializeMoves(moves));

        switch (game.playerStatus()) {
            case NORMAL -> Gdx.app.log(LOG_TAG, "The game continues like normal...");
            case CHECKED -> Gdx.app.log(LOG_TAG, "Check! " + sideToMove + " king is in danger!");
            case CHECKMATED -> Gdx.app.log(LOG_TAG, "Checkmate! " + sideToMove.opposite() + " player won!");
            case STALEMATED -> Gdx.app.log(LOG_TAG, "Stalemate! The game ends in a draw!");
        }
    }

    void save(Game game) {
        settings.pgn(game.pgn());
        settings.fen(game.fen());
    }
}
