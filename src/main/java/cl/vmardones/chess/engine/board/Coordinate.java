/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A coordinate is one of the 64 positions where a chess piece can be. It's usually identified by
 * the chess algebraic notation, which consists of the coordinate's rank (a-h) folowed by its column
 * (1-8).
 */
public final class Coordinate {

    private static final List<Coordinate> CACHED_COORDINATES = fillCoordinateCache();

    private final int index;

    /* Coordinate creation */

    /**
     * Create a coordinate, indicating its position with chess algebraic notation.
     * Although this method is public, in general you'll prefer to use methods that shorten the syntax.
     *
     * @param algebraicNotation The algebraic notation of the coordinate.
     * @return The created coordinate.
     * @throws InvalidCoordinateException If the coordinate isn't inside the allowed chessboard.
     */
    public static Coordinate of(String algebraicNotation) {
        return CACHED_COORDINATES.get(AlgebraicConverter.toIndex(algebraicNotation));
    }

    /* Getters */

    /**
     * Obtains the column of this coordinate. The column is a letter between lowercase a (left side or
     * queen's side) and lowercase h (right side or king's side).
     *
     * @return The coordinate's column.
     */
    public char column() {
        return toString().charAt(0);
    }

    /**
     * Obtains the column of this coordinate as a number. The a column's index is 0 and the h column's
     * index is 8.
     *
     * @return The coordinate's column, as an index.
     */
    public int columnIndex() {
        return index % Board.SIDE_LENGTH;
    }

    /**
     * Obtains the rank (row in chess terminology) of this coordinate. The rank is a number between 1
     * (bottom of the board, white side) and 8 (top of the board, black side).
     *
     * @return The coordinate's rank.
     */
    public int rank() {
        return Board.SIDE_LENGTH - index / Board.SIDE_LENGTH;
    }

    /* Comparisons */

    /**
     * Compares this coordinate with another, to see if they are on the same column.
     *
     * @param other The other coordinate.
     * @return True if both are on the same column.
     */
    public boolean sameColumnAs(Coordinate other) {
        return column() == other.column();
    }

    /**
     * Compares this coordinate with another, to see if they are on the same rank.
     *
     * @param other The other coordinate.
     * @return True if both are on the same rank.
     */
    public boolean sameRankAs(Coordinate other) {
        return rank() == other.rank();
    }

    /* Traslation operations */

    /**
     * Gets the coordinate at a relative position.
     *
     * @param x X axis movement, positive goes right.
     * @param y Y axis movement, positive goes up.
     * @return Coordinate at the relative position, if it is inside the board.
     */
    public @Nullable Coordinate to(int x, int y) {
        try {
            var destination = CACHED_COORDINATES.get(index + horizontalClamp(x) - y * Board.SIDE_LENGTH);

            if (illegalJump(x, destination)) {
                return null;
            }

            return destination;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get the coordinate X spaces upwards from this coordinate.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate at the relative position, if it is inside the board.
     */
    public @Nullable Coordinate up(int spaces) {
        return to(0, spaces);
    }

    /**
     * Get the coordinate X spaces downwards from this coordinate.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate at the relative position, if it is inside the board.
     */
    public @Nullable Coordinate down(int spaces) {
        return up(-spaces);
    }

    /**
     * Get the coordinate X spaces to the left of this coordinate.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate at the relative position, if it is inside the board.
     */
    public @Nullable Coordinate left(int spaces) {
        return right(-spaces);
    }

    /**
     * Get the coordinate X spaces to the right of this coordinate.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate at the relative position, if it is inside the board.
     */
    public @Nullable Coordinate right(int spaces) {
        return to(spaces, 0);
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

        var other = (Coordinate) o;
        return index == other.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    /**
     * Represent the coordinate in algebraic notation.
     *
     * @return The coordinate's algebraic notation.
     */
    @Override
    public String toString() {
        return AlgebraicConverter.toAlgebraic(index);
    }

    /* Coordinate creation (package-private) */

    static Coordinate of(int index) {
        if (isOutsideBoard(index)) {
            throw new InvalidCoordinateException("Index is outside chessboard: " + index);
        }

        return CACHED_COORDINATES.get(index);
    }

    int index() {
        return index;
    }

    private static List<Coordinate> fillCoordinateCache() {
        return IntStream.range(Board.MIN_SQUARES, Board.MAX_SQUARES)
                .mapToObj(Coordinate::new)
                .toList();
    }

    private Coordinate(int index) {
        this.index = index;
    }

    private static boolean isOutsideBoard(int index) {
        return index < Board.MIN_SQUARES || index >= Board.MAX_SQUARES;
    }

    private boolean illegalJump(int x, Coordinate destination) {
        return x < 0 && destination.columnIndex() > columnIndex() || x > 0 && destination.columnIndex() < columnIndex();
    }

    private int horizontalClamp(int x) {
        return x % Board.SIDE_LENGTH;
    }
}
