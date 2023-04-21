/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import java.io.Serial;

final class IllegalSaveException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3052143176397355598L;

    IllegalSaveException() {
        super("Current game state is null, so it cannot be saved!");
    }
}
