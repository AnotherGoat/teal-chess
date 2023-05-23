/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.uci;

import java.io.IOException;

public final class UciLauncher {

    public static void main(String... args) throws IOException {
        new UciCommunicator().start();
    }

    private UciLauncher() {}
}
