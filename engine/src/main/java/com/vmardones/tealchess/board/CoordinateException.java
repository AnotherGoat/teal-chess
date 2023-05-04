/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import java.io.Serial;

/**
 * Exception thrown when normal program execution tries to access a coordinate outside the chessboard.
 */
public final class CoordinateException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4216598041578910238L;

    CoordinateException() {
        super("The coordinate was expected to be inside the chessboard, but it wasn't");
    }
}
