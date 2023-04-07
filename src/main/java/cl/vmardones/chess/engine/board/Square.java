/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cl.vmardones.chess.engine.piece.Piece;
import cl.vmardones.chess.engine.player.Alliance;
import org.eclipse.jdt.annotation.Nullable;

/** A single chess square, which may or may not contain a piece. */
public final class Square {

    private static final Map<String, Square> CACHED_EMPTY_SQUARES = fillEmptySquareCache();

    private final Coordinate coordinate;
    private final Alliance color;
    private final @Nullable Piece piece;

    /* Square creation */

    /**
     * Static factory method for creating a new square.
     *
     * @param algebraicNotation Algebraic notaiton for the position of the square.
     * @param piece The piece on the square.
     * @return A new square.
     */
    public static Square create(String algebraicNotation, @Nullable Piece piece) {
        if (piece == null) {
            return CACHED_EMPTY_SQUARES.get(algebraicNotation);
        }

        return new Square(algebraicNotation, piece);
    }

    /* Getters */

    public Coordinate coordinate() {
        return coordinate;
    }

    public Alliance color() {
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
            return "-";
        }

        return piece.singleChar();
    }

    private static Map<String, Square> fillEmptySquareCache() {
        record Entry(String algebraicNotation, Square square) {}

        return IntStream.range(Board.MIN_SQUARES, Board.MAX_SQUARES)
                .mapToObj(AlgebraicConverter::toAlgebraic)
                .map(algebraicNotation -> new Entry(algebraicNotation, new Square(algebraicNotation, null)))
                .collect(Collectors.toMap(Entry::algebraicNotation, Entry::square));
    }

    private Square(Coordinate coordinate) {
        this(coordinate, null);
    }

    private Square(String algebraicNotation, @Nullable Piece piece) {
        this(Coordinate.of(algebraicNotation), piece);
    }

    private Square(Coordinate coordinate, @Nullable Piece piece) {
        this.coordinate = coordinate;
        this.piece = piece;
        color = assignColor();
    }

    private Alliance assignColor() {
        if ((coordinate.index() + coordinate.index() / Board.SIDE_LENGTH) % 2 == 0) {
            return Alliance.WHITE;
        }

        return Alliance.BLACK;
    }
}
