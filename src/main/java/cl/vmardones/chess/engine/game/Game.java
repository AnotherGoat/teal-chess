/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.analysis.BoardAnalyzer;
import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.BoardDirector;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveMaker;
import cl.vmardones.chess.engine.move.MoveResult;
import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.engine.player.Player;
import org.eclipse.jdt.annotation.Nullable;

public final class Game {

    private static final Logger LOG = LogManager.getLogger(Game.class);
    private BoardAnalyzer boardAnalyzer;
    private final MoveMaker moveMaker;
    private final GameState state;
    private GameHistory history;

    public Game() {
        LOG.info("Game started!\n");

        moveMaker = new MoveMaker();
        state = new GameState();
        history = new GameHistory();

        registerTurn(createFirstTurn());
    }

    /* Getters */

    public Board board() {
        return state.currentTurn().board();
    }

    public Player currentPlayer() {
        return state.currentTurn().player();
    }

    public Player currentOpponent() {
        return state.currentTurn().opponent();
    }

    public GameHistory history() {
        return history;
    }

    public void addTurn(Move move) {
        var nextTurnBoard = moveMaker.make(board(), move);
        var nextTurn = createTurn(nextTurnBoard, currentOpponent().color(), move);
        registerTurn(nextTurn);
    }

    /* Analysis methods */

    public MoveResult testMove(Move move) {
        return boardAnalyzer.testMove(move);
    }

    public List<Move> findLegalMoves(Piece piece) {
        return boardAnalyzer.findLegalMoves(piece);
    }

    private void registerTurn(Turn turn) {
        state.currentTurn(turn);
        var memento = state.save();
        history = history.add(memento);
        LOG.debug("Move added to history: {}\n", memento.state().lastMove());
    }

    private Turn createFirstTurn() {
        return createTurn(BoardDirector.createStandardBoard(), Color.WHITE, null);
    }

    private Turn createTurn(Board board, Color activeColor, @Nullable Move lastMove) {
        LOG.debug("Current gameboard:\n{}", board);
        LOG.debug("White king: {}", board.king(Color.WHITE));
        LOG.debug("White pieces: {}", board.pieces(Color.WHITE));
        LOG.debug("Black king: {}", board.king(Color.BLACK));
        LOG.debug("Black pieces: {}", board.pieces(Color.BLACK));
        LOG.debug("En passant pawn: {}\n", board.enPassantPawn());

        boardAnalyzer = new BoardAnalyzer(board, activeColor);
        var whitePlayer = boardAnalyzer.createPlayer(Color.WHITE);
        var blackPlayer = boardAnalyzer.createPlayer(Color.BLACK);

        LOG.debug("Players: {} vs. {}", whitePlayer, blackPlayer);
        LOG.debug("White legals: {}", whitePlayer.legals());
        LOG.debug("Black legals: {}", blackPlayer.legals());

        return new Turn(board, activeColor, whitePlayer, blackPlayer, lastMove);
    }
}
