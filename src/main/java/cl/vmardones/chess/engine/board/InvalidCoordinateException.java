/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

final class InvalidCoordinateException extends RuntimeException {

    InvalidCoordinateException(String message) {
        super(message);
    }
}
