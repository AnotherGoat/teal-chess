/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

/**
 * An object that can be represented using Standard Algebraic Notation (SAN).
 * @see <a href="https://www.chessprogramming.org/Algebraic_Chess_Notation#Standard_Algebraic_Notation_.28SAN.29">Standard Algebraic Notation</a>
 */
public interface San {

    /**
     * Exports this object in SAN notation.
     * @return String representation in SAN.
     */
    String san();
}
