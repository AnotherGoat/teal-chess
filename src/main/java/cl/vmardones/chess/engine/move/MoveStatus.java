/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

// TODO: Merge MoveStatus and MoveResult
/** Tells whether a move is possible or not, including the reason. This is meant to be used when testing moves. */
public enum MoveStatus {
    /** Can be made and the game continues normally. */
    NORMAL,
    /** Leaves the opponent in check. */
    CHECKS,
    /** Violates the game's rules, cannot be made. */
    ILLEGAL,
    /** Both the source and destination are the same, which means this isn't a move. */
    NONE
}
