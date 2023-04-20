/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io;

import java.awt.*;
import javax.swing.*;

import com.vmardones.tealchess.ExcludeFromGeneratedReport;
import com.vmardones.tealchess.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

public final class PieceIconLoader {

    private static final String PIECE_ICON_PATH = "art/piece/cburnett";

    public static @Nullable ImageIcon load(Piece piece, int width, int height) {

        var baseIcon = SvgLoader.load(formatIconPath(piece));

        if (baseIcon == null) {
            return null;
        }

        var scaledImage = baseIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private static String formatIconPath(Piece piece) {
        return "%s/%s%s.svg"
                .formatted(PIECE_ICON_PATH, piece.color(), piece.firstChar());
    }

    @ExcludeFromGeneratedReport
    private PieceIconLoader() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
