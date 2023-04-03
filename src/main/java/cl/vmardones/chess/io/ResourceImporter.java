/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.io.InputStream;
import org.eclipse.jdt.annotation.Nullable;

final class ResourceImporter {

  private ResourceImporter() {
    throw new UnsupportedOperationException("You cannot instantiate me!");
  }

  static @Nullable InputStream get(String path) {
    return ResourceImporter.class.getClassLoader().getResourceAsStream(path);
  }
}
