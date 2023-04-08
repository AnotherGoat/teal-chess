/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import org.eclipse.jdt.annotation.Nullable;

public final class SvgLoader {

    private static final Logger LOG = LogManager.getLogger(SvgLoader.class);

    public static @Nullable BufferedImage load(String path, int width, int height) {

        var iconResource = ResourceImporter.get(path);

        try {
            if (iconResource == null) {
                LOG.warn("Could not load the SVG file {}", path);
                return null;
            }

            return SvgImporter.get(iconResource, width, height);
        } catch (IOException e) {
            LOG.warn("Could not load the SVG file {}", path);
            return null;
        }
    }

    @ExcludeFromGeneratedReport
    private SvgLoader() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
