/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.parser;

final class FenParseException extends RuntimeException {

    FenParseException(String message) {
        super(message);
    }
}
