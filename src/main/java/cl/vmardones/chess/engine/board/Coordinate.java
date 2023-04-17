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
 * A coordinate is one of the 64 places where a square can be found. It's usually identified by
 * the chess algebraic notation, which consists of the coordinate's rank (a-h) folowed by its file
 * (1-8).
 */
public final class Coordinate {

    private static final List<Coordinate> COORDINATE_CACHE = fillCoordinateCache();

    private final int index;

    /* Coordinate creation */

    /**
     * Create a coordinate using chess algebraic notation.
     * Although this method is public, in general you'll prefer to use methods that shorten the syntax.
     *
     * @param algebraicNotation The algebraic notation of the coordinate.
     * @return The created coordinate.
     */
    public static Coordinate of(String algebraicNotation) {
        return COORDINATE_CACHE.get(AlgebraicConverter.toIndex(algebraicNotation));
    }

    /* Getters */

    /**
     * Obtains the file (row in chess terminology) of this coordinate. The file is a letter between lowercase a (left side or
     * queen's side) and lowercase h (right side or king's side).
     *
     * @return The coordinate's file.
     * @see <a href="https://www.chessprogramming.org/Files">Files</a>
     */
    public String file() {
        return toString().substring(0, 1);
    }

    /**
     * Obtains the rank (row in chess terminology) of this coordinate. The rank is a number between 1
     * (bottom of the board, white side) and 8 (top of the board, black side).
     *
     * @return The coordinate's rank.
     * @see <a href="https://www.chessprogramming.org/Ranks">Ranks</a>
     */
    public int rank() {
        return Board.SIDE_LENGTH - index / Board.SIDE_LENGTH;
    }

    /* Comparisons */

    /**
     * Compares this coordinate with another, to see if they are on the same file.
     *
     * @param other The other coordinate.
     * @return True if both are on the same file.
     */
    public boolean sameFileAs(Coordinate other) {
        return file().equals(other.file());
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
     * Moves the coordinate to a relative location.
     *
     * @param x X axis movement, positive goes right.
     * @param y Y axis movement, positive goes up.
     * @return Coordinate after the move, only if it is inside the board.
     */
    public @Nullable Coordinate to(int x, int y) {
        try {
            var destination = COORDINATE_CACHE.get(index + horizontalClamp(x) - y * Board.SIDE_LENGTH);

            if (illegalJump(x, destination)) {
                return null;
            }

            return destination;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get the coordinate X spaces upwards from this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate after the move, only if it is inside the board.
     */
    public @Nullable Coordinate up(int spaces) {
        return to(0, spaces);
    }

    /**
     * Get the coordinate X spaces downwards from this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate after the move, only if it is inside the board.
     */
    public @Nullable Coordinate down(int spaces) {
        return up(-spaces);
    }

    /**
     * Get the coordinate X spaces to the left of this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate after the move, only if it is inside the board.
     */
    public @Nullable Coordinate left(int spaces) {
        return right(-spaces);
    }

    /**
     * Get the coordinate X spaces to the right of this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Coordinate after the move, only if it is inside the board.
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
     * Get the algebraic notation representation of this coordinate.
     *
     * @return The coordinate's algebraic notation.
     */
    @Override
    public String toString() {
        return AlgebraicConverter.toAlgebraic(index);
    }

    int index() {
        return index;
    }

    private static List<Coordinate> fillCoordinateCache() {
        return IntStream.range(Board.FIRST_SQUARE_INDEX, Board.MAX_SQUARES)
                .mapToObj(Coordinate::new)
                .toList();
    }

    private Coordinate(int index) {
        this.index = index;
    }

    private boolean illegalJump(int x, Coordinate destination) {
        return x < 0 && destination.fileIndex() > fileIndex() || x > 0 && destination.fileIndex() < fileIndex();
    }

    private int fileIndex() {
        return index % Board.SIDE_LENGTH;
    }

    private int horizontalClamp(int x) {
        return x % Board.SIDE_LENGTH;
    }
}
