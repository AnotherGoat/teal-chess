/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.square;

import java.io.Serial;

/**
 * Exception thrown when incorrect algebraic notation is used.
 */
public final class AlgebraicNotationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8460280404931066099L;

    AlgebraicNotationException(String message) {
        super(message);
    }
}
