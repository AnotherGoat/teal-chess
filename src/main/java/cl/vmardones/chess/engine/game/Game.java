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
import cl.vmardones.chess.engine.player.BlackPlayer;
import cl.vmardones.chess.engine.player.Player;
import cl.vmardones.chess.engine.player.WhitePlayer;
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
    gameState.setCurrentTurn(turn);
    gameHistory.add(gameState.save());
  }

  private Turn createFirstTurn() {
    return createTurn(boardService.createStandardBoard(), Alliance.WHITE);
  }

  private Turn createTurn(Board board, Alliance nextMoveMaker) {
    var whiteLegals = calculateWhiteLegals(board);
    LOG.debug("White legals: {}", whiteLegals);
    var blackLegals = calculateBlackLegals(board);
    LOG.debug("Black legals: {}", blackLegals);

    var whitePlayer = new WhitePlayer(board, board.whiteKing(), whiteLegals, blackLegals);
    LOG.debug("White player: {}", whitePlayer);
    var blackPlayer = new BlackPlayer(board, board.blackKing(), blackLegals, whiteLegals);
    LOG.debug("Black player: {}", blackPlayer);

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
    return createTurn(move.execute(), getOpponent().getAlliance());
  }

  public Board getBoard() {
    return gameState.getCurrentTurn().board();
  }

  public Player getCurrentPlayer() {
    return gameState.getCurrentTurn().getPlayer();
  }

  /**
   * Obtains the player in the other side of the board.
   *
   * @return The opponent
   */
  public Player getOpponent() {
    return gameState.getCurrentTurn().getOpponent();
  }

  public Player getWhitePlayer() {
    return gameState.getCurrentTurn().whitePlayer();
  }

  public Player getBlackPlayer() {
    return gameState.getCurrentTurn().blackPlayer();
  }

  public MoveTransition performMove(Move move) {
    return getCurrentPlayer().makeMove(getCurrentPlayer(), move);
  }
}
