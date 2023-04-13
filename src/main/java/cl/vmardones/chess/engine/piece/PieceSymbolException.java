/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

/**
 * Exception thrown when someone tries to create a piece by passing an unknown symbol.
 */
public final class PieceSymbolException extends RuntimeException {
    PieceSymbolException(String symbol) {
        super("Unknown piece symbol: " + symbol);
    }
}
