/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import org.eclipse.jdt.annotation.Nullable;

public final class SvgLoader {

    private static final Logger LOG = LogManager.getLogger(SvgLoader.class);
    private static final int BASE_SIZE = 500;
    private static final Map<String, ImageIcon> SVG_CACHE = new HashMap<>();

    public static @Nullable ImageIcon load(String path, int width, int height) {

        var baseIcon = load(path);

        if (baseIcon == null) {
            return null;
        }

        var scaledImage = baseIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    static @Nullable ImageIcon load(String path) {

        if (SVG_CACHE.containsKey(path)) {
            LOG.debug("Loaded from SVG cache {}", path);
            return SVG_CACHE.get(path);
        }

        var iconResource = ResourceImporter.get(path);

        if (iconResource == null) {
            LOG.warn("Could not load the SVG file {}", path);
            return null;
        }

        try {
            var svgImage = SvgImporter.get(iconResource, BASE_SIZE, BASE_SIZE);

            if (svgImage == null) {
                return null;
            }

            var icon = new ImageIcon(svgImage);
            SVG_CACHE.put(path, icon);
            return icon;
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
