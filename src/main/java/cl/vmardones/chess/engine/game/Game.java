/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.BoardService;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.move.MoveTransition;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.HumanPlayer;
import cl.vmardones.chess.engine.player.Player;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

  private static final Logger LOG = LoggerFactory.getLogger(Game.class);

  private final GameState gameState;
  private final GameHistory gameHistory;
  private final BoardService boardService;

  public Game() {
    LOG.info("Game started!");

    gameState = new GameState();
    gameHistory = new GameHistory();
    boardService = new BoardService();

    registerTurn(createFirstTurn());
  }

  private void registerTurn(Turn turn) {
    gameState.currentTurn(turn);
    gameHistory.add(gameState.save());
  }

  private Turn createFirstTurn() {
    return createTurn(boardService.createStandardBoard(), Alliance.WHITE);
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
    return boardService.calculateLegals(board, board.whitePieces());
  }

  private List<Move> calculateBlackLegals(Board board) {
    return boardService.calculateLegals(board, board.blackPieces());
  }

  public Turn createNextTurn(Move move) {
    return createTurn(move.execute(), getOpponent().alliance());
  }

  public Board getBoard() {
    return gameState.currentTurn().board();
  }

  public Player getCurrentPlayer() {
    return gameState.currentTurn().getPlayer();
  }

  /**
   * Obtains the player in the other side of the board.
   *
   * @return The opponent
   */
  public Player getOpponent() {
    return gameState.currentTurn().getOpponent();
  }

  public Player getWhitePlayer() {
    return gameState.currentTurn().whitePlayer();
  }

  public Player getBlackPlayer() {
    return gameState.currentTurn().blackPlayer();
  }

  public MoveTransition performMove(Move move) {
    return getCurrentPlayer().makeMove(getCurrentPlayer(), move);
  }
}
