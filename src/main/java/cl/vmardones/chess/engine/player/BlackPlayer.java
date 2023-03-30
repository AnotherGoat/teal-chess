/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.King;
import cl.vmardones.chess.engine.piece.Piece;
import java.util.List;

/** The player that uses the black pieces. */
public class BlackPlayer extends Player {

  public BlackPlayer(Board board, King king, List<Move> legals, List<Move> opponentMoves) {
    super(board, king, legals, opponentMoves);
  }

  @Override
  public List<Piece> getActivePieces() {
    return board.getBlackPieces();
  }

  @Override
  public Alliance getAlliance() {
    return Alliance.BLACK;
  }
}
