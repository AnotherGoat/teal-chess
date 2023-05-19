/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.parser.fen.Fen;
import com.vmardones.tealchess.parser.pgn.San;
import com.vmardones.tealchess.square.AlgebraicConverter;

// TODO: Add a piece cache
// TODO: Add a "coordinate" method, which is meant to be used by the frontend
/**
 * A chess piece, which players can move in the board.
 * @see <a href="https://www.chessprogramming.org/Pieces">Pieces</a>
 */
public record Piece(PieceType type, Color color, int square) implements Fen, San, Unicode {

    /* Static factory methods */

    /**
     * Alternative method to build a piece, useful for parsing.
     * Only valid symbols are "PNBRQK" for white pieces and "pnbrqk" for black pieces;
     * The color is inferred from the symbol being uppercase (white) or lowercase (black).
     *
     * @param symbol The piece symbol.
     * @param square The square to put the piece in.
     * @return The piece with the asked symbol.
     */
    public static Piece fromSymbol(String symbol, int square) {
        var color = Character.isUpperCase(symbol.charAt(0)) ? Color.WHITE : Color.BLACK;

        return new Piece(PieceType.fromSymbol(symbol), color, square);
    }

    /* Getters */

    @Override
    public String unicode() {
        return color.isWhite() ? type.whiteUnicode() : type.blackUnicode();
    }

    @Override
    public String fen() {
        return color.isWhite() ? type.whiteFen() : type.blackFen();
    }

    /**
     * Represent this piece in SAN notation.
     * In most contexts, pawns don't have a SAN abbreviation.
     * If "P" is needed to represent a pawn, use singleChar instead.
     * @return String representation of this piece in SAN movetext.
     */
    @Override
    public String san() {
        return type.whiteFen();
    }

    /* Checking piece types */

    public boolean isPawn() {
        return type == PieceType.PAWN;
    }

    public boolean isKnight() {
        return type == PieceType.KNIGHT;
    }

    public boolean isBishop() {
        return type == PieceType.BISHOP;
    }

    public boolean isRook() {
        return type == PieceType.ROOK;
    }

    public boolean isQueen() {
        return type == PieceType.QUEEN;
    }

    public boolean isKing() {
        return type == PieceType.KING;
    }

    /* toString */

    @Override
    public String toString() {
        return unicode() + AlgebraicConverter.toAlgebraic(square);
    }
}
