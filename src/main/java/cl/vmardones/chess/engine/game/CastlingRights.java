/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import cl.vmardones.chess.engine.player.Color;

/**
 * Specifies the precondition of what castle moves could be possible, based on what kings and rooks have been moved.
 * @see <a href="https://www.chessprogramming.org/Castling_Rights">Castling Rights</a>
 */
public record CastlingRights(
        boolean whiteKingSide, boolean whiteQueenSide, boolean blackKingSide, boolean blackQueenSide) {

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
    public String toString() {
        if (!whiteKingSide && !whiteQueenSide && !blackKingSide && !blackQueenSide) {
            return "-";
        }

        return String.format(
                "%s%s%s%s",
                whiteKingSide ? "K" : "",
                whiteQueenSide ? "Q" : "",
                blackKingSide ? "k" : "",
                blackQueenSide ? "q" : "");
    }
}
