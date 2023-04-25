/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

/**
 * An object that can be represented using chess Unicode characters.
 * Please note that this is, for the most part, a custom notation and it's not part of any standard notation.
 */
public interface Unicode {

    /**
     * Exports this object using Unicode characters.
     * @return String representation using Unicode characters.
     */
    String unicode();
}
