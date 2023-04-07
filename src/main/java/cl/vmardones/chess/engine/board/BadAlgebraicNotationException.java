/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

final class BadAlgebraicNotationException extends RuntimeException {

    BadAlgebraicNotationException(String message) {
        super(message);
    }
}
