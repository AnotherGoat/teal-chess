/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

final class FontLoader {

    private static final String FONT_PATH = "fonts";

    static BitmapFont load(String name, int size, Color color) {
        var path = "%s/%s.ttf".formatted(FONT_PATH, name);

        var file = Gdx.files.internal(path);
        var generator = new FreeTypeFontGenerator(file);

        var parameters = new FreeTypeFontParameter();
        parameters.size = size;
        parameters.color = color;

        return generator.generateFont(parameters);
    }

    private FontLoader() {}
}
