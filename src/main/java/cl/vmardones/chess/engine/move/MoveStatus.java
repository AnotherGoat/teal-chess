/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

/** Tells whether the move is possible or not, including the reason. */
public enum MoveStatus {
  DONE,
  NULL,
  ILLEGAL,
  LEAVES_OPPONENT_IN_CHECK;

  public boolean isDone() {
    return this == DONE;
  }
}
