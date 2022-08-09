/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import jakarta.validation.constraints.NotNull;
import java.awt.*;
import java.io.IOException;
import javax.swing.plaf.FontUIResource;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FontLoader {

  private static final String FONT_PATH = "fonts/";
  public static final int FONT_SIZE = 16;
  public static final Font SYSTEM_FONT = new FontUIResource(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);

  @Generated
  private FontLoader() {
    throw new UnsupportedOperationException("You cannot instantiate me!");
  }

  public static Font load(@NotNull final String fontName) {
    try {
      final var fontResource = ResourceImporter.get(FONT_PATH + fontName);

      return Font.createFont(Font.TRUETYPE_FONT, fontResource).deriveFont(Font.PLAIN, FONT_SIZE);

    } catch (final FontFormatException | IOException e) {
      log.warn("Could not load the font {}, defaulting to system font", fontName);
      return SYSTEM_FONT;
    }
  }
}
