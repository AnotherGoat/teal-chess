/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.io.InputStream;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import org.eclipse.jdt.annotation.Nullable;

final class ResourceImporter {

    static @Nullable InputStream get(String path) {
        return ResourceImporter.class.getClassLoader().getResourceAsStream(path);
    }

    @ExcludeFromGeneratedReport
    private ResourceImporter() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
