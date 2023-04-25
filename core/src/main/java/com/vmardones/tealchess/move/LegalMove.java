/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

public final class LegalMove {

    private final Move move;
    // TODO: Actually implement this, it should be used for PGN notation hash (+ for check, # for checkmate)
    private final MoveResult result;

    LegalMove(Move move, MoveResult result) {
        this.move = move;
        this.result = result;
    }

    /* Getters */

    public Move move() {
        return move;
    }

    /* toString */

    @Override
    public String toString() {
        return move.toString() + result;
    }
}
