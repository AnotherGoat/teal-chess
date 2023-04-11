/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

/** Tells what happens after a move is made and whether that move is possible or not. Because illegal moves are never saved, the ILLEGAL and NONE results are only used when testing moves. */
public enum MoveResult {
    /** The move can be made and the game continues normally. */
    NORMAL,
    /** Leaves the opponent in check. */
    CHECKS("+"),
    /** Checkmates the opponent. The game finishes. */
    CHECKMATES("#"),
    /** Violates the game's rules, cannot be made. */
    ILLEGAL,
    /** Both the source and destination are the same, which means this isn't a move. */
    NONE;

    private final String endHash;

    /**
     * The end hash used by this result, according to PGN notation.
     * @return The end hash.
     */
    public String endHash() {
        return endHash;
    }

    MoveResult() {
        endHash = "";
    }

    MoveResult(String endHash) {
        this.endHash = endHash;
    }
}
