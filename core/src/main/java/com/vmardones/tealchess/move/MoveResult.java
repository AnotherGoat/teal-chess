/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

// TODO: This is essentially the same as PlayerStatus, merge both classes

import com.vmardones.tealchess.parser.San;

/** Tells what happens after a move is made and whether that move is possible or not. Because illegal moves are never saved, the ILLEGAL and NONE results are only used when testing moves. */
public enum MoveResult implements San {
    /** The move can be made and the game continues normally. */
    CONTINUE(""),
    /** Leaves the opponent in check. */
    CHECKS("+"),
    /** Checkmates the opponent. The game finishes. */
    CHECKMATES("#"),
    /** Leaves the opponent without moves, but doesn't checkmate. The game finishes. */
    STALEMATES("");

    private final String endHash;

    /**
     * The end hash used by this result, used in SAN movetext notation.
     * @return The end hash.
     */
    @Override
    public String san() {
        return endHash;
    }

    MoveResult(String endHash) {
        this.endHash = endHash;
    }
}
