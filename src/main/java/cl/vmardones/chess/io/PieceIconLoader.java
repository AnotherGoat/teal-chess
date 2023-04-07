/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.io.IOException;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.engine.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

public final class PieceIconLoader {

    private static final Logger LOG = LogManager.getLogger(PieceIconLoader.class);
    private static final String PIECE_ICON_PATH = "art/pieces";

    public static @Nullable Icon load(Piece piece, int width, int height) {

        var iconResource = ResourceImporter.get(getIconPath(piece));

        try {
            if (iconResource == null) {
                LOG.warn("Could not load the piece {}", getIconPath(piece));
                return null;
            }

            return new ImageIcon(SvgImporter.get(iconResource, width, height));
        } catch (IOException e) {
            LOG.warn("Could not load the piece {}", getIconPath(piece));
            return null;
        }
    }

    private static String getIconPath(Piece piece) {
        return "%s/%s%s.svg"
                .formatted(PIECE_ICON_PATH, piece.alliance(), piece.singleChar().toLowerCase());
    }

    private PieceIconLoader() {
        throw new UnsupportedOperationException("You cannot instantiate me!");
    }
}
