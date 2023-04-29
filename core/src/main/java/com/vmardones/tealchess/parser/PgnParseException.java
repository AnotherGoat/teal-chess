/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser;

import java.io.Serial;

final class PgnParseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2918698635040326671L;

    PgnParseException(String message) {
        super(message);
    }
}
