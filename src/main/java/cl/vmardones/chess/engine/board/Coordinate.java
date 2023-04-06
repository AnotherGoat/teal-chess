/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import cl.vmardones.chess.engine.player.Alliance;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A coordinate is one of the 64 positions where a chess piece can be. It's usually identified by
 * the chess algebraic notation, which consists of the coordinate's rank (a-h) folowed by its column
 * (1-8).
 */
public final class Coordinate {

    private static final Pattern ALGEBRAIC_PATTERN = Pattern.compile("^[a-h][1-8]$");
    private static final List<Coordinate> CACHED_COORDINATES = createCoordinateCache();

    private final int index;

    /* Coordinate creation */

    /**
     * Create a coordinate, using an array index. Generally used when creating every coordinate, one
     * by one.
     *
     * @param index The array index of the coordinate.
     * @return The created coordinate.
     * @throws InvalidCoordinateException If the coordinate is outside the allowed chessboard.
     */
    public static Coordinate of(int index) {
        if (isOutsideBoard(index)) {
            throw new InvalidCoordinateException("Index is outside chessboard: " + index);
        }

        return CACHED_COORDINATES.get(index);
    }

    /**
     * Create a coordinate, indicating its position with chess algebraic notation.
     *
     * @param algebraic The algebraic notation of the coordinate.
     * @return The created coordinate.
     * @throws InvalidCoordinateException If the coordinate isn't inside the allowed chessboard.
     */
    public static Coordinate of(String algebraic) {
        if (!ALGEBRAIC_PATTERN.matcher(algebraic).matches()) {
            throw new InvalidCoordinateException("Invalid algebraic notation: " + algebraic);
        }

        return CACHED_COORDINATES.get(calculateIndex(algebraic));
    }

    /* Getters */

    /**
     * Get the array index of this coordinate. Indexes start at 0 in the a8 coordinate, and end at 63
     * in the h1 coordinate.
     *
     * @return This coordinate's index.
     */
    public int index() {
        return index;
    }

    /**
     * Obtains the column of this coordinate. The column is a letter between lowercase a (left side or
     * queen's side) and lowercase h (right side or king's side).
     *
     * @return The coordinate's column.
     */
    public char column() {
        return Column.getByIndex(columnIndex());
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

    /**
     * Obtains the color of this coordinate.
     *
     * @return The color, which is either black or white.
     */
    public Alliance color() {
        if ((index + index / Board.SIDE_LENGTH) % 2 == 0) {
            return Alliance.WHITE;
        }

        return Alliance.BLACK;
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

    /**
     * Compares this coordinate with another, to see if they have the same color.
     *
     * @param other The other coordinate.
     * @return True if both are the same color.
     */
    public boolean sameColorAs(Coordinate other) {
        return color() == other.color();
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

    private boolean illegalJump(int x, Coordinate destination) {
        return x < 0 && destination.columnIndex() > columnIndex() || x > 0 && destination.columnIndex() < columnIndex();
    }

    private int horizontalClamp(int x) {
        return x % Board.SIDE_LENGTH;
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
        return String.valueOf(column()) + rank();
    }

    private static List<Coordinate> createCoordinateCache() {
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

    private static int calculateIndex(String algebraicCoordinate) {
        var column = Column.indexOf(algebraicCoordinate.charAt(0));
        var rank = Board.SIDE_LENGTH
                * (Board.SIDE_LENGTH - Integer.parseInt(String.valueOf(algebraicCoordinate.charAt(1))));

        return column + rank;
    }

    private enum Column {
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H;

        private static final List<Character> names =
                Arrays.stream(values()).map(Column::getName).toList();

        private static char getByIndex(int index) {
            return values()[index].getName();
        }

        private static int indexOf(Character name) {
            return names.indexOf(name);
        }

        private char getName() {
            return name().toLowerCase().charAt(0);
        }
    }
}
