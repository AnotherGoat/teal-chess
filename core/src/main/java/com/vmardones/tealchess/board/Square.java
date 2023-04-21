/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.player.Color;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A single chess square, which may or may not contain a piece.
 * @see <a href="https://www.chessprogramming.org/Squares">Squares</a>
 */
public final class Square {

    private static final Map<String, Square> EMPTY_SQUARE_CACHE = fillEmptySquareCache();

    private final Coordinate coordinate;
    private final Color color;
    private final @Nullable Piece piece;

    /* Square creation */

    /**
     * Static factory method for creating a new square.
     *
     * @param coordinate Coordinate of the square, in algebraic notation.
     * @param piece The piece on the square.
     * @return A new square.
     */
    public static Square create(String coordinate, @Nullable Piece piece) {
        if (piece == null) {
            return EMPTY_SQUARE_CACHE.get(coordinate);
        }

        return new Square(coordinate, piece);
    }

    /* Getters */

    public Coordinate coordinate() {
        return coordinate;
    }

    public Color color() {
        return color;
    }

    public @Nullable Piece piece() {
        return piece;
    }

    /* Comparing squares */

    /**
     * Compares this square with another, to see if they have the same color.
     *
     * @param other The other square.
     * @return True if both are the same color.
     */
    public boolean sameColorAs(Square other) {
        return color() == other.color();
    }

    /* equals, hashCode and toString */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (Square) o;
        return coordinate.equals(other.coordinate) && color.equals(other.color) && Objects.equals(piece, other.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, color, piece);
    }

    /**
     * String representation of this square, used when displaying the board.
     *
     * @return The character that represents the piece, or - if the square is empty.
     */
    @Override
    public String toString() {
        if (piece == null) {
            return color == Color.WHITE ? "□" : "■";
        }

        return piece.unicodeChar();
    }

    private static Map<String, Square> fillEmptySquareCache() {
        record Entry(String coordinate, Square square) {}

        return IntStream.range(Board.FIRST_SQUARE_INDEX, Board.MAX_SQUARES)
                .mapToObj(AlgebraicConverter::toAlgebraic)
                .map(coordinate -> new Entry(coordinate, new Square(coordinate)))
                .collect(toMap(Entry::coordinate, Entry::square));
    }

    private Square(String coordinate) {
        this(coordinate, null);
    }

    private Square(String coordinate, @Nullable Piece piece) {
        this(Coordinate.of(coordinate), piece);
    }

    private Square(Coordinate coordinate, @Nullable Piece piece) {
        this.coordinate = coordinate;
        this.piece = piece;
        color = assignColor();
    }

    private Color assignColor() {
        if ((coordinate.index() + coordinate.index() / Board.SIDE_LENGTH) % 2 == 0) {
            return Color.WHITE;
        }

        return Color.BLACK;
    }
}
