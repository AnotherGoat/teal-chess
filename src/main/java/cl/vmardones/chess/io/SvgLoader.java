/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.annotation.Nullable;

@Slf4j
public final class SvgLoader {

  @Generated
  private SvgLoader() {
    throw new UnsupportedOperationException("You cannot instantiate me!");
  }

  public static @Nullable BufferedImage load(String path, int width, int height) {

    var iconResource = ResourceImporter.get(path);

    try {
      if (iconResource == null) {
        log.warn("Could not load the SVG file {}", path);
        return null;
      }

      return SvgImporter.get(iconResource, width, height);
    } catch (IOException e) {
      log.warn("Could not load the SVG file {}", path);
      return null;
    }
  }
}
