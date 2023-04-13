/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

/**
 * Exception thrown when someone tries to get an alliance by passing an unknown symbol.
 */
public final class AllianceSymbolException extends RuntimeException {
    AllianceSymbolException(String symbol) {
        super("Unknown alliance symbol: " + symbol);
    }
}
