/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.coordinate;

import java.io.Serial;

/**
 * Exception thrown when incorrect algebraic notation is used.
 */
public final class AlgebraicConverterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8460280404931066099L;

    AlgebraicConverterException(String message) {
        super(message);
    }
}
