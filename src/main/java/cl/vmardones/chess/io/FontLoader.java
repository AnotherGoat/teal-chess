/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.awt.*;
import java.io.IOException;
import javax.swing.plaf.FontUIResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FontLoader {

  public static final int FONT_SIZE = 16;
  public static final Font SYSTEM_FONT = new FontUIResource(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);

  private static final Logger LOG = LoggerFactory.getLogger(FontLoader.class);
  private static final String FONT_PATH = "fonts/";

  private FontLoader() {
    throw new UnsupportedOperationException("You cannot instantiate me!");
  }

  public static Font load(String fontName) {
    try {
      var fontResource = ResourceImporter.get(FONT_PATH + fontName);

      if (fontResource == null) {
        LOG.warn("Could not load the font {}, defaulting to system font", fontName);
        return SYSTEM_FONT;
      }

      return Font.createFont(Font.TRUETYPE_FONT, fontResource).deriveFont(Font.PLAIN, FONT_SIZE);

    } catch (FontFormatException | IOException e) {
      LOG.warn("Could not load the font {}, defaulting to system font", fontName);
      return SYSTEM_FONT;
    }
  }
}
