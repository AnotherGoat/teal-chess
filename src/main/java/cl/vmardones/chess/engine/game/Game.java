/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.BoardService;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveMaker;
import cl.vmardones.chess.engine.move.MoveTransition;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.HumanPlayer;
import cl.vmardones.chess.engine.player.Player;

public final class Game {

    private static final Logger LOG = LogManager.getLogger(Game.class);

    private final GameState state;
    private GameHistory history;

    public Game() {
        LOG.info("Game started!");

        state = new GameState();
        history = new GameHistory();

        registerTurn(createFirstTurn());
    }

    // TODO: Check if this method is actually needed, because its return value is never used and it doesn't modify
    // internal state
    public Turn createNextTurn(Move move) {
        var nextTurnBoard = MoveMaker.make(board(), move);
        return createTurn(nextTurnBoard, currentOpponent().alliance());
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

    public MoveTransition makeMove(Move move) {
        return currentPlayer().makeMove(currentPlayer(), move);
    }

    private void registerTurn(Turn turn) {
        state.currentTurn(turn);
        history = history.add(state.save());
    }

    private Turn createFirstTurn() {
        return createTurn(BoardService.createStandardBoard(), Alliance.WHITE);
    }

    private Turn createTurn(Board board, Alliance nextMoveMaker) {
        LOG.debug("Current gameboard:\n{}", board);
        LOG.debug("White king: {}", board.whiteKing());
        LOG.debug("White pieces: {}", board.whitePieces());
        LOG.debug("Black king: {}", board.blackKing());
        LOG.debug("Black pieces: {}", board.blackPieces());
        LOG.debug("En passant pawn: {}\n", board.enPassantPawn());

        var whiteLegals = calculateWhiteLegals(board);
        var blackLegals = calculateBlackLegals(board);
        var whitePlayer = new HumanPlayer(Alliance.WHITE, board, whiteLegals, blackLegals);
        var blackPlayer = new HumanPlayer(Alliance.BLACK, board, blackLegals, whiteLegals);

        LOG.debug("Players: {} vs. {}", whitePlayer, blackPlayer);
        LOG.debug("White legals: {}", whitePlayer.legals());
        LOG.debug("Black legals: {}", blackPlayer.legals());

        var turn = new Turn(board, nextMoveMaker, whitePlayer, blackPlayer);
        registerTurn(turn);

        return turn;
    }

    private List<Move> calculateWhiteLegals(Board board) {
        return BoardService.calculateLegals(board, board.whitePieces());
    }

    private List<Move> calculateBlackLegals(Board board) {
        return BoardService.calculateLegals(board, board.blackPieces());
    }
}
