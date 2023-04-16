/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.piece;

import java.util.List;
import java.util.Objects;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.player.Color;

/**
 * A chess piece, which players can move in the board.
 * @see <a href="https://www.chessprogramming.org/Pieces">Pieces</a>
 */
public abstract sealed class Piece permits JumpingPiece, SlidingPiece {

    protected final PieceType type;
    protected final Coordinate coordinate;
    protected final Color color;
    protected final boolean firstMove;

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
        var upperCaseSymbol = symbol.toUpperCase();

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

    public boolean firstMove() {
        return firstMove;
    }

    public String singleChar() {
        var singleChar = getClass().getSimpleName().substring(0, 1);

        return color == Color.BLACK ? singleChar.toLowerCase() : singleChar;
    }

    public abstract String unicodeChar();

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

    public abstract List<Coordinate> calculatePossibleDestinations(Board board);

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
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (Piece) o;
        return type == other.type
                && coordinate.equals(other.coordinate)
                && color == other.color
                && firstMove == other.firstMove;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(type, coordinate, color, firstMove);
    }

    @Override
    public String toString() {
        return String.format("%s%s", unicodeChar(), coordinate);
    }

    protected Piece(PieceType type, String coordinate, Color color, boolean firstMove) {
        this.type = type;
        this.coordinate = Coordinate.of(coordinate);
        this.color = color;
        this.firstMove = firstMove;
    }
}
