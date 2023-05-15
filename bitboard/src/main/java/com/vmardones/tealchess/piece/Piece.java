/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.parser.fen.Fen;
import com.vmardones.tealchess.parser.pgn.San;

// TODO: Add a piece cache
/**
 * A chess piece, which players can move in the board.
 * @see <a href="https://www.chessprogramming.org/Pieces">Pieces</a>
 */
public record Piece(PieceType type, Color color, int coordinate) implements Fen, San, Unicode {

    /* Static factory methods */

    /**
     * Alternative method to build a piece, useful for parsing.
     * Only valid symbols are "PNBRQK" for white pieces and "pnbrqk" for black pieces;
     * The color is inferred from the symbol being uppercase (white) or lowercase (black).
     *
     * @param symbol The piece symbol.
     * @param coordinate The coordinate to put the piece in.
     * @return The piece with the asked symbol.
     */
    public static Piece fromSymbol(String symbol, int coordinate) {
        var color = Character.isUpperCase(symbol.charAt(0)) ? Color.WHITE : Color.BLACK;

        return new Piece(PieceType.fromSymbol(symbol), color, coordinate);
    }

    /* Getters */

    /**
     * Checks whether a piece slides when moving or not.
     * Sliding pieces can move freely in a direction until they're blocked by other pieces and they can easily pin enemy pieces, like a Rook.
     * Non-sliding pieces have a fixed set of possible destinations, like a Knight.
     * @see <a href="https://www.chessprogramming.org/Sliding_Pieces">Sliding Pieces</a>
     * @return True if the piece slides.
     */
    public boolean sliding() {
        return type.sliding();
    }

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
        return isPawn() ? "" : type.whiteFen();
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
        return unicode() + coordinate;
    }
}
