/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmardones.tealchess.analysis.PositionAnalyzer;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import com.vmardones.tealchess.player.Player;

public final class Game {

    private static final Logger LOG = LogManager.getLogger(Game.class);
    private final MoveMaker moveMaker;
    private final GameState state;
    private PositionAnalyzer positionAnalyzer;
    private GameHistory history;
    private Player whitePlayer;
    private Player blackPlayer;

    public Game() {
        LOG.info("Game started!\n");

        moveMaker = new MoveMaker();
        state = new GameState();
        history = new GameHistory();
        positionAnalyzer = new PositionAnalyzer(state.currentPosition());
        whitePlayer = positionAnalyzer.createPlayer(Color.WHITE);
        blackPlayer = positionAnalyzer.createPlayer(Color.BLACK);

        registerPosition(state.currentPosition());
    }

    /* Getters */

    public Board board() {
        return state.currentPosition().board();
    }

    public Player currentPlayer() {
        return state.currentPosition().sideToMove().player(players());
    }

    public Player currentOpponent() {
        return state.currentPosition().sideToMove().opponent(players());
    }

    public GameHistory history() {
        return history;
    }

    public void updatePosition(Move move) {
        var nextPosition = moveMaker.make(state.currentPosition(), move);

        positionAnalyzer = new PositionAnalyzer(nextPosition);
        whitePlayer = positionAnalyzer.createPlayer(Color.WHITE);
        blackPlayer = positionAnalyzer.createPlayer(Color.BLACK);

        registerPosition(nextPosition);
    }

    /* Analysis methods */

    /**
     * Given a piece, find the legal moves it has for this position. Mainly used when the user clicks on a piece, to highlight its legal moves.
     * @param piece The piece to move.
     * @return The legal moves of the piece.
     */
    public List<Move> findLegalMoves(Piece piece) {
        return positionAnalyzer.findLegalMoves(piece);
    }

    private List<Player> players() {
        return List.of(whitePlayer, blackPlayer);
    }

    private void registerPosition(Position position) {
        analyzePosition(position);

        state.currentPosition(position);
        history = history.add(state.save());

        LOG.debug("Move added to history: {}\n", state.currentPosition().lastMove());

        if (whitePlayer.legals().isEmpty() && blackPlayer.legals().isEmpty()) {
            LOG.info(
                    "Checkmate! {} player won!\n",
                    position.sideToMove().opposite().name());
        }
    }

    private void analyzePosition(Position position) {

        var board = position.board();

        LOG.debug("Current chessboard:\n{}", board);

        LOG.debug("White king: {}", board.king(Color.WHITE));
        LOG.debug("White pieces: {}", board.pieces(Color.WHITE));
        LOG.debug("Black king: {}", board.king(Color.BLACK));
        LOG.debug("Black pieces: {}", board.pieces(Color.BLACK));
        LOG.debug("Castling rights: {}", position.castlingRights());
        LOG.debug("En passant pawn: {}\n", position.enPassantTarget());

        LOG.debug("Players: {} vs. {}", whitePlayer, blackPlayer);
        var sideToMove = position.sideToMove();
        LOG.debug("Side to move: {}", sideToMove.name());
        LOG.debug("Legal moves: {}", sideToMove.isWhite() ? whitePlayer.legals() : blackPlayer.legals());
    }
}
