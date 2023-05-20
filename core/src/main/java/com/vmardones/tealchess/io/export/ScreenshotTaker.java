/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.export;

import java.util.zip.Deflater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

public final class ScreenshotTaker {

    private static final String SCREENSHOT_PATH = "teal-chess/screenshots/";

    public static String save(String fileName) {
        return save(fileName, 0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
    }

    public static String save(String fileName, int x, int y, int width, int height) {
        var pixmap = Pixmap.createFromFrameBuffer(x, y, width, height);
        var pixels = pixmap.getPixels();

        var size = width * height * 4;
        for (var i = 3; i < size; i += 4) {
            pixels.put(i, (byte) 255);
        }

        var path = SCREENSHOT_PATH + fileName + ".png";
        var fileHandle = Gdx.files.external(path);

        PixmapIO.writePNG(fileHandle, pixmap, Deflater.DEFAULT_COMPRESSION, true);
        pixmap.dispose();

        return fileHandle.path();
    }

    private ScreenshotTaker() {}
}
