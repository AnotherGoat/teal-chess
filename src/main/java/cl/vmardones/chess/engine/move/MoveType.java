/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

public enum MoveType {
  /** A move where a non-pawn piece moves to an empty tile. */
  NORMAL,
  /** A move where a non-pawn piece captures another piece. */
  CAPTURE,
  /**
   * A move where a pawn moves to an empty tile. Promotion can happen if the opposite side of the
   * board is reached by this move.
   */
  PAWN_NORMAL,
  /** A special move where a pawn can jump an extra tile during its first move. * */
  PAWN_JUMP,
  /** A move where a pawn attacks in diagonal to capture another piece. */
  PAWN_CAPTURE,
  /**
   * A special move where a pawn takes down another pawn which tried to evade its attack by jumping.
   */
  EN_PASSANT,
  /** A special move where the king enters the king-side castle and moves 2 tiles. */
  KING_CASTLE,
  /** A special move where the king enters the queen-side castle and moves 3 tiles. */
  QUEEN_CASTLE
}
