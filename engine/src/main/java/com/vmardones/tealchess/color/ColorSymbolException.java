/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.color;

import java.io.Serial;

/**
 * Exception thrown when someone tries to get a piece color by passing an unknown symbol.
 */
public final class ColorSymbolException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4914178604547704652L;

    ColorSymbolException(String symbol) {
        super("Unknown color symbol: " + symbol);
    }
}
