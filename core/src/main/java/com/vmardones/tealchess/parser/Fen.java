/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

/**
 * An object that can be represented using Forsyth-Edwards Notation (FEN).
 * @see <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">Forsyth-Edwards Notation</a>
 */
public interface Fen {

    /**
     * Exports this object in FEN notation.
     * @return String representation in FEN.
     */
    String fen();
}
