/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.BoardService;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveMaker;
import cl.vmardones.chess.engine.move.MoveStatus;
import cl.vmardones.chess.engine.move.MoveTester;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.Player;
import cl.vmardones.chess.engine.player.PlayerFactory;
import org.eclipse.jdt.annotation.Nullable;

public final class Game {

    private static final Logger LOG = LogManager.getLogger(Game.class);
    private final MoveMaker moveMaker;
    private final GameState state;
    private GameHistory history;

    public Game() {
        LOG.info("Game started!");

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
        var nextTurn = createTurn(nextTurnBoard, currentOpponent().alliance(), move);
        registerTurn(nextTurn);
    }

    public MoveStatus testMove(Move move) {
        return MoveTester.testMove(
                move,
                currentPlayer().king(),
                currentPlayer().legals(),
                currentOpponent().legals());
    }

    private void registerTurn(Turn turn) {
        state.currentTurn(turn);
        var memento = state.save();
        history = history.add(memento);
        LOG.debug("Move saved: {}", memento.state().lastMove());
    }

    private Turn createFirstTurn() {
        return createTurn(BoardService.createStandardBoard(), Alliance.WHITE, null);
    }

    private Turn createTurn(Board board, Alliance nextMoveMaker, @Nullable Move lastMove) {
        LOG.debug("Current gameboard:\n{}", board);
        LOG.debug("White king: {}", board.king(Alliance.WHITE));
        LOG.debug("White pieces: {}", board.pieces(Alliance.WHITE));
        LOG.debug("Black king: {}", board.king(Alliance.BLACK));
        LOG.debug("Black pieces: {}", board.pieces(Alliance.BLACK));
        LOG.debug("En passant pawn: {}\n", board.enPassantPawn());

        var whitePlayer = new PlayerFactory(board, Alliance.WHITE).create();
        var blackPlayer = new PlayerFactory(board, Alliance.BLACK).create();

        LOG.debug("Players: {} vs. {}", whitePlayer, blackPlayer);
        LOG.debug("White legals: {}", whitePlayer.legals());
        LOG.debug("Black legals: {}", blackPlayer.legals());

        return new Turn(board, nextMoveMaker, whitePlayer, blackPlayer, lastMove);
    }
}
