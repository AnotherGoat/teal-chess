/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io;

import java.awt.*;
import java.io.IOException;
import javax.swing.plaf.FontUIResource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmardones.tealchess.ExcludeFromGeneratedReport;

public final class FontLoader {

    public static final int FONT_SIZE = 16;
    public static final Font SYSTEM_FONT = new FontUIResource(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);

    private static final Logger LOG = LogManager.getLogger(FontLoader.class);
    private static final String FONT_PATH = "fonts/";

    public static Font load(String fontName) {

        var fontResource = ResourceImporter.get(FONT_PATH + fontName);

        if (fontResource == null) {
            LOG.warn("Could not load the font {}, defaulting to system font", fontName);
            return SYSTEM_FONT;
        }

        try {
            return Font.createFont(Font.TRUETYPE_FONT, fontResource).deriveFont(Font.PLAIN, FONT_SIZE);
        } catch (FontFormatException | IOException e) {
            LOG.warn("Could not load the font {}, defaulting to system font", fontName);
            return SYSTEM_FONT;
        }
    }

    @ExcludeFromGeneratedReport
    private FontLoader() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
