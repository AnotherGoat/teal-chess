/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.io.Serial;

/**
 * Exception thrown when someone tries to create a piece by passing an unknown symbol.
 */
public final class PieceSymbolException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -290251136150273225L;

    PieceSymbolException(String symbol) {
        super("Unknown piece symbol: " + symbol);
    }
}
