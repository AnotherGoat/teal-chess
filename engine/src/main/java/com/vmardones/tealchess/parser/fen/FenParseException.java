/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.fen;

import java.io.Serial;

final class FenParseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4669405947512808528L;

    FenParseException(String message) {
        super(message);
    }
}
