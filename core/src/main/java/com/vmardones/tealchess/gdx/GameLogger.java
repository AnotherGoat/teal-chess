/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import com.badlogic.gdx.Gdx;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.settings.SettingsManager;
import com.vmardones.tealchess.parser.FenSerializer;
import com.vmardones.tealchess.parser.PgnSerializer;
import com.vmardones.tealchess.player.Color;

final class GameLogger {

    private static final String LOG_TAG = "Game";
    private final SettingsManager settings;

    public GameLogger(SettingsManager settings) {
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

        var position = game.position();
        Gdx.app.debug(LOG_TAG, "Castling rights: " + position.castlingRights().fen());

        var enPassantTarget = position.enPassantTarget();
        if (enPassantTarget != null) {
            Gdx.app.debug(LOG_TAG, "En passant target: " + enPassantTarget.unicode() + enPassantTarget.coordinate());
        }

        var player = game.player();
        Gdx.app.debug(LOG_TAG, "Players: " + player + " vs. " + game.oppponent());

        var sideToMove = position.sideToMove();
        Gdx.app.log(LOG_TAG, sideToMove + "'s turn!");
        Gdx.app.debug(LOG_TAG, "Legal moves: " + player.legals());

        var moves = game.history().moves();
        Gdx.app.debug(LOG_TAG, "Move history: " + PgnSerializer.serializeMoves(moves));

        switch (game.player().status()) {
            case NORMAL -> Gdx.app.log(LOG_TAG, "The game continues like normal...");
            case CHECKED -> Gdx.app.log(LOG_TAG, "Check! " + sideToMove + " king is in danger!");
            case CHECKMATED -> Gdx.app.log(LOG_TAG, "Checkmate! " + sideToMove.opposite() + " player won!");
            case STALEMATED -> Gdx.app.log(LOG_TAG, "Stalemate! The game ends in a draw!");
        }
    }

    void save(Game game) {
        settings.pgn(PgnSerializer.serializeGame(game));
        settings.fen(FenSerializer.serialize(game.position()));
    }
}
