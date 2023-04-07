/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

/**
 * What happens after a move is completed.
 * Useful for saving move logs.
 */
public enum MoveResult {
    /** After a move is made, the game continues normally. */
    CONTINUE(""),
    /** After a move is made, the other player is checked. */
    CHECK("+"),
    /** After a move is made, the other player is checkmated. */
    CHECKMATE("#");

    private final String endHash;

    /**
     * The end hash used by this result, according to PGN notation.
     * @return The end hash.
     */
    public String endHash() {
        return endHash;
    }

    MoveResult(String endHash) {
        this.endHash = endHash;
    }
}
