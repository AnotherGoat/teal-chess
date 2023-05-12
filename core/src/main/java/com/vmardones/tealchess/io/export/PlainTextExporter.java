/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.export;

import com.badlogic.gdx.Gdx;

public final class PlainTextExporter {

    private static final String PLAIN_TEXT_PATH = "teal-chess/text/";

    public static String save(String filename, String text) {
        var fullPath = PLAIN_TEXT_PATH + filename;
        Gdx.files.external(fullPath).writeString(text + "\n", false);
        return fullPath;
    }

    private PlainTextExporter() {}
}
