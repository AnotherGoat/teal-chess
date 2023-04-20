/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

final class IllegalSaveException extends RuntimeException {

    IllegalSaveException() {
        super("Current game state is null, so it cannot be saved!");
    }
}