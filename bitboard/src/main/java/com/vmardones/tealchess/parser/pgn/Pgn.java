/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.pgn;

/**
 * An object that can be represented using Portable Game Notation (PGN).
 * @see <a href="https://www.chessprogramming.org/Portable_Game_Notation">Portable Game Notation</a>
 */
public interface Pgn {
    String pgn();
}
