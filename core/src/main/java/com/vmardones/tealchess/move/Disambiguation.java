/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

/**
 * Disambiguation used when representing legal moves in SAN movetext, where two pieces of the same kind can get to the same destination.
 * It rarely happens, so most of the time it will be NONE.
 * Due to the rules of the game, Pawn and King moves never require disambiguation.
 * @see <a href="https://www.chessprogramming.org/Algebraic_Chess_Notation#Ambiguities">Ambiguities</a>
 */
public enum Disambiguation {
    /** First case of disambiguation.
     * Used when the rank is the same, but the file can be used to distinguish both moves. */
    FILE,
    /** Second case of disambiguation.
     * Used when the file is the same, but the rank can be used to distinguish both moves. */
    RANK,
    /** Final case of disambiguation.
     * Used when both the file and the rank are different. */
    FULL
}
