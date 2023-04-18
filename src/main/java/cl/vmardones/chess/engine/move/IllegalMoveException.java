/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.move;

public final class IllegalMoveException extends RuntimeException {
    IllegalMoveException(String message) {
        super(message);
    }
}