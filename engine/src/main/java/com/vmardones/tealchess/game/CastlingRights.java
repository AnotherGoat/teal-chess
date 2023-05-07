/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import com.vmardones.tealchess.parser.fen.Fen;
import com.vmardones.tealchess.player.Color;

/**
 * Specifies the precondition of what castle moves could be possible, based on what kings and rooks have been moved.
 * @see <a href="https://www.chessprogramming.org/Castling_Rights">Castling Rights</a>
 */
public record CastlingRights(
        boolean whiteKingSide, boolean whiteQueenSide, boolean blackKingSide, boolean blackQueenSide) implements Fen {

    public CastlingRights() {
        this(false, false, false, false);
    }

    public CastlingRights disable(Color color) {
        return switch (color) {
            case WHITE -> new CastlingRights(false, false, blackKingSide, blackQueenSide);
            case BLACK -> new CastlingRights(whiteKingSide, whiteQueenSide, false, false);
        };
    }

    public CastlingRights disableKingSide(Color color) {
        return switch (color) {
            case WHITE -> new CastlingRights(false, whiteQueenSide, blackKingSide, blackQueenSide);
            case BLACK -> new CastlingRights(whiteKingSide, whiteQueenSide, false, blackQueenSide);
        };
    }

    public CastlingRights disableQueenSide(Color color) {
        return switch (color) {
            case WHITE -> new CastlingRights(whiteKingSide, false, blackKingSide, blackQueenSide);
            case BLACK -> new CastlingRights(whiteKingSide, whiteQueenSide, blackKingSide, false);
        };
    }

    @Override
    public String fen() {
        if (!whiteKingSide && !whiteQueenSide && !blackKingSide && !blackQueenSide) {
            return "-";
        }

        return String.join(
                "",
                whiteKingSide ? "K" : "",
                whiteQueenSide ? "Q" : "",
                blackKingSide ? "k" : "",
                blackQueenSide ? "q" : "");
    }
}
