/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at COPYRIGHT in the root directory.
 */

package cl.vmardones.chess.engine.player;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.game.Game;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;
import java.util.Collection;

/** The player that uses the black pieces. */
public class BlackPlayer extends Player {

  public BlackPlayer(
      final Board board,
      final King king,
      final Game game,
      final Collection<Move> legals,
      final Collection<Move> opponentMoves) {
    super(board, king, game, legals, opponentMoves);
  }

  @Override
  public Collection<Piece> getActivePieces() {
    return board.getBlackPieces();
  }

  @Override
  public Alliance getAlliance() {
    return Alliance.BLACK;
  }

  @Override
  public Player getOpponent() {
    return game.getWhitePlayer();
  }
}
