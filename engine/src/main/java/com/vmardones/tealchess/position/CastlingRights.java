/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.position;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.parser.fen.Fen;

/**
 * Specifies the precondition of what castle moves could be possible, based on what kings and rooks have been moved or captured.
 * @see <a href="https://www.chessprogramming.org/Castling_Rights">Castling Rights</a>
 */
public record CastlingRights(boolean whiteShort, boolean whiteLong, boolean blackShort, boolean blackLong)
        implements Fen {

    public CastlingRights() {
        this(false, false, false, false);
    }

    public CastlingRights disable(Color color) {
        return switch (color) {
            case WHITE -> new CastlingRights(false, false, blackShort, blackLong);
            case BLACK -> new CastlingRights(whiteShort, whiteLong, false, false);
        };
    }

    public CastlingRights disableShortCastle(Color color) {
        return switch (color) {
            case WHITE -> new CastlingRights(false, whiteLong, blackShort, blackLong);
            case BLACK -> new CastlingRights(whiteShort, whiteLong, false, blackLong);
        };
    }

    public CastlingRights disableLongCastle(Color color) {
        return switch (color) {
            case WHITE -> new CastlingRights(whiteShort, false, blackShort, blackLong);
            case BLACK -> new CastlingRights(whiteShort, whiteLong, blackShort, false);
        };
    }

    @Override
    public String fen() {
        if (!whiteShort && !whiteLong && !blackShort && !blackLong) {
            return "-";
        }

        return String.join(
                "", whiteShort ? "K" : "", whiteLong ? "Q" : "", blackShort ? "k" : "", blackLong ? "q" : "");
    }

    @Override
    public String toString() {
        return fen();
    }
}
