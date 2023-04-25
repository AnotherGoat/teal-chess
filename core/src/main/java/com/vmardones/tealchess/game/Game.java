/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmardones.tealchess.analysis.PositionAnalyzer;
import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.LegalMove;
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
        positionAnalyzer = new PositionAnalyzer(state.position());
        whitePlayer = positionAnalyzer.createPlayer(Color.WHITE);
        blackPlayer = positionAnalyzer.createPlayer(Color.BLACK);

        registerPosition(state.position());
    }

    /* Getters */

    public Board board() {
        return state.position().board();
    }

    public Player currentPlayer() {
        return state.position().sideToMove().player(players());
    }

    public Player currentOpponent() {
        return state.position().sideToMove().opponent(players());
    }

    public GameHistory history() {
        return history;
    }

    public void updatePosition(LegalMove move) {
        state.lastMove(move);

        var nextPosition = moveMaker.make(state.position(), move);

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
    public Set<Coordinate> findLegalDestinations(Piece piece) {
        return positionAnalyzer.findLegalMoves(piece).stream()
                .map(legal -> legal.move().destination())
                .collect(toUnmodifiableSet());
    }

    private List<Player> players() {
        return List.of(whitePlayer, blackPlayer);
    }

    private void registerPosition(Position position) {
        analyzePosition(position);

        state.position(position);
        history = history.add(state.save());

        LOG.debug("Move history: {}", history.moves());

        switch (currentPlayer().status()) {
            case NORMAL -> LOG.info("The game continues like normal...\n");
            case CHECKED -> LOG.info(
                    "{0}'s turn!\nCheck! {} king is in danger!\n",
                    position.sideToMove().name());
            case CHECKMATED -> LOG.info(
                    "Checkmate! {} player won!\n",
                    position.sideToMove().opposite().name());
            case STALEMATED -> LOG.info("Stalemate! The game ends in a draw!\n");
        }
    }

    private void analyzePosition(Position position) {

        var board = position.board();

        LOG.debug("Current chessboard:\n{}", board.unicode());

        LOG.debug("White king: {}", board.king(Color.WHITE));
        LOG.debug("White pieces: {}", board.pieces(Color.WHITE));
        LOG.debug("Black king: {}", board.king(Color.BLACK));
        LOG.debug("Black pieces: {}", board.pieces(Color.BLACK));
        LOG.debug("Castling rights: {}", position.castlingRights().fen());
        LOG.debug("En passant pawn: {}\n", position.enPassantTarget());

        LOG.debug("Players: {} vs. {}\n", whitePlayer, blackPlayer);
        var sideToMove = position.sideToMove();
        LOG.info("{}'s turn!", sideToMove.name());
        LOG.debug("Legal moves: {}", sideToMove.isWhite() ? whitePlayer.legals() : blackPlayer.legals());
    }
}
