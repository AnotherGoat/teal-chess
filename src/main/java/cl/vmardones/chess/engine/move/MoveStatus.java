/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

/** Tells whether the move is possible or not, including the reason. */
public enum MoveStatus {
  /** Can be performed and the game continues normally. */
  DONE,
  /** Leaves the opponent in check. */
  CHECKS,
  /**
   * Violates the game's rules, cannot be performed.
   */
  ILLEGAL,
  /**
   * Both the source and destination are the same, which means this isn't a move.
   */
  NONE
}
