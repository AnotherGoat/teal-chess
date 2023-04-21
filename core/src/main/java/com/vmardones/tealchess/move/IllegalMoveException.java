/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.move;

import java.io.Serial;

public final class IllegalMoveException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6183535357046678687L;

    IllegalMoveException(String message) {
        super(message);
    }
}
