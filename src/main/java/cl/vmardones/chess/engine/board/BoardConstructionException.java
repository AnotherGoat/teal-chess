/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

/**
 * Exception thrown when board build steps are misused.
 */
public final class BoardConstructionException extends RuntimeException {
    BoardConstructionException(String message) {
        super(message);
    }
}
