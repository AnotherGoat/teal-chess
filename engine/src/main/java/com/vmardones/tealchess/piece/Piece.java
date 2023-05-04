/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.parser.Fen;
import com.vmardones.tealchess.parser.San;
import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A chess piece, which players can move in the board.
 * @see <a href="https://www.chessprogramming.org/Pieces">Pieces</a>
 */
public abstract sealed class Piece implements Fen, San, Unicode permits Bishop, King, Knight, Pawn, Queen, Rook {

    protected final PieceType type;
    protected final Coordinate coordinate;
    protected final Color color;
    protected final boolean sliding;
    protected final List<Vector> moveVectors;

    /* Alternate piece construction */

    /**
     * Alternative method to build a piece, useful for parsing.
     * Only valid symbols are "PNBRQK" for white pieces and "pnbrqk" for black pieces;
     * The color is inferred from the symbol being uppercase (white) or lowercase (black).
     *
     * @param symbol The piece symbol.
     * @param coordinate The coordinate to put the piece in.
     * @return The piece with the asked symbol.
     */
    public static Piece fromSymbol(String symbol, String coordinate) {
        var color = Character.isUpperCase(symbol.charAt(0)) ? Color.WHITE : Color.BLACK;
        var upperCaseSymbol = symbol.toUpperCase(Locale.ROOT);

        return switch (upperCaseSymbol) {
            case "P" -> new Pawn(coordinate, color);
            case "N" -> new Knight(coordinate, color);
            case "B" -> new Bishop(coordinate, color);
            case "R" -> new Rook(coordinate, color);
            case "Q" -> new Queen(coordinate, color);
            case "K" -> new King(coordinate, color);
            default -> throw new PieceSymbolException(symbol);
        };
    }

    /* Getters */

    public Coordinate coordinate() {
        return coordinate;
    }

    public Color color() {
        return color;
    }

    /**
     * Checks whether a piece slides when moving or not.
     * Sliding pieces can move freely in a direction until they're blocked by other pieces and they can easily pin enemy pieces, like a Rook.
     * Non-sliding pieces have a fixed set of possible destinations, like a Knight.
     * @see <a href="https://www.chessprogramming.org/Sliding_Pieces">Sliding Pieces</a>
     * @return True if the piece slides.
     */
    public boolean sliding() {
        return sliding;
    }

    public List<Vector> moveVectors() {
        return moveVectors;
    }

    public String firstChar() {
        return type.firstChar();
    }

    @Override
    public String fen() {
        return color.isBlack() ? firstChar().toLowerCase(Locale.ROOT) : firstChar();
    }

    /**
     * Represent this piece in SAN notation.
     * In most contexts, pawns don't have a SAN abbreviation.
     * If "P" is needed to represent a pawn, use singleChar instead.
     * @return String representation of this piece in SAN movetext.
     */
    @Override
    public String san() {
        return isPawn() ? "" : type.firstChar();
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

    /* Comparing pieces */

    public boolean isAllyOf(Piece other) {
        return color() == other.color();
    }

    public boolean isEnemyOf(Piece other) {
        return !isAllyOf(other);
    }

    /* Movement */

    /**
     * Move this piece to another square. No checks of any kind are done to check whether the move is
     * legal or not.
     *
     * @param destination The destination to move the piece to.
     * @return The piece after the move is completed.
     */
    public abstract Piece moveTo(String destination);

    /* equals, hashCode and toString */

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Piece other)) {
            return false;
        }

        return type == other.type
                && coordinate.equals(other.coordinate)
                && color == other.color
                && sliding == other.sliding
                && moveVectors.equals(other.moveVectors);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(type, coordinate, color, sliding, moveVectors);
    }

    @Override
    public String toString() {
        return unicode() + coordinate;
    }

    protected Piece(PieceType type, String coordinate, Color color, List<Vector> moveVectors, boolean sliding) {
        this.type = type;
        this.coordinate = Coordinate.of(coordinate);
        this.color = color;
        this.moveVectors = moveVectors;
        this.sliding = sliding;
    }
}
