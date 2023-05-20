/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.export;

import com.badlogic.gdx.Gdx;

public final class PlainTextExporter {

    private static final String PLAIN_TEXT_PATH = "teal-chess/text/";

    public static String save(String fileName, String text) {
        var fullPath = PLAIN_TEXT_PATH + fileName;
        var fileHandle = Gdx.files.external(fullPath);

        fileHandle.writeString(text + "\n", false);

        return fileHandle.path();
    }

    private PlainTextExporter() {}
}
