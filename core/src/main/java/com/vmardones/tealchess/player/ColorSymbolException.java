/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

/**
 * Exception thrown when someone tries to get a piece color by passing an unknown symbol.
 */
public final class ColorSymbolException extends RuntimeException {
    ColorSymbolException(String symbol) {
        super("Unknown color symbol: " + symbol);
    }
}
