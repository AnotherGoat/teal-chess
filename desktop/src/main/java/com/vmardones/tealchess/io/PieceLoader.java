/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io;

import com.badlogic.gdx.graphics.Pixmap;
import com.vmardones.tealchess.ExcludeFromGeneratedReport;

public final class PieceLoader {

    private static final String PIECE_ICON_PATH = "art/piece";
    private static final int TEXTURE_SIZE = 64;

    public static Pixmap load(PieceTheme theme, String pieceCode) {
        return SvgLoader.load(formatIconPath(theme, pieceCode), TEXTURE_SIZE);
    }

    private static String formatIconPath(PieceTheme theme, String pieceCode) {
        return "%s/%s/%s.svg".formatted(PIECE_ICON_PATH, theme, pieceCode);
    }

    @ExcludeFromGeneratedReport
    private PieceLoader() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
