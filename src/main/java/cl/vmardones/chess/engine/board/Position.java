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
 * A position is one of the 64 positions where a chess piece can be. It's usually identified by
 * the chess algebraic notation, which consists of the position's rank (a-h) folowed by its file
 * (1-8).
 */
public final class Position {

    private static final List<Position> POSITION_CACHE = fillPositionCache();

    private final int index;

    /* Position creation */

    /**
     * Create a position, indicating its position with chess algebraic notation.
     * Although this method is public, in general you'll prefer to use methods that shorten the syntax.
     *
     * @param algebraicNotation The algebraic notation of the position.
     * @return The created position.
     */
    public static Position of(String algebraicNotation) {
        return POSITION_CACHE.get(AlgebraicConverter.toIndex(algebraicNotation));
    }

    /* Getters */

    /**
     * Obtains the file (row in chess terminology) of this position. The file is a letter between lowercase a (left side or
     * queen's side) and lowercase h (right side or king's side).
     *
     * @return The position's file.
     */
    public char file() {
        return toString().charAt(0);
    }

    /**
     * Obtains the rank (row in chess terminology) of this post. The rank is a number between 1
     * (bottom of the board, white side) and 8 (top of the board, black side).
     *
     * @return The position's rank.
     */
    public int rank() {
        return Board.SIDE_LENGTH - index / Board.SIDE_LENGTH;
    }

    /* Comparisons */

    /**
     * Compares this position with another, to see if they are on the same file.
     *
     * @param other The other position.
     * @return True if both are on the same file.
     */
    public boolean sameFileAs(Position other) {
        return file() == other.file();
    }

    /**
     * Compares this position with another, to see if they are on the same rank.
     *
     * @param other The other position.
     * @return True if both are on the same rank.
     */
    public boolean sameRankAs(Position other) {
        return rank() == other.rank();
    }

    /* Traslation operations */

    /**
     * Moves the position to a relative location.
     *
     * @param x X axis movement, positive goes right.
     * @param y Y axis movement, positive goes up.
     * @return Position after the move, only if it is inside the board.
     */
    public @Nullable Position to(int x, int y) {
        try {
            var destination = POSITION_CACHE.get(index + horizontalClamp(x) - y * Board.SIDE_LENGTH);

            if (illegalJump(x, destination)) {
                return null;
            }

            return destination;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get the position X spaces upwards from this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Position after the move, only if it is inside the board.
     */
    public @Nullable Position up(int spaces) {
        return to(0, spaces);
    }

    /**
     * Get the position X spaces downwards from this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Position after the move, only if it is inside the board.
     */
    public @Nullable Position down(int spaces) {
        return up(-spaces);
    }

    /**
     * Get the position X spaces to the left of this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Position after the move, only if it is inside the board.
     */
    public @Nullable Position left(int spaces) {
        return right(-spaces);
    }

    /**
     * Get the position X spaces to the right of this one.
     *
     * @param spaces Number of spaces to jump, it can also be negative to move backwards.
     * @return Position after the move, only if it is inside the board.
     */
    public @Nullable Position right(int spaces) {
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

        var other = (Position) o;
        return index == other.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    /**
     * Get the algebraic notation representation of this position.
     *
     * @return The position's algebraic notation.
     */
    @Override
    public String toString() {
        return AlgebraicConverter.toAlgebraic(index);
    }

    int index() {
        return index;
    }

    private static List<Position> fillPositionCache() {
        return IntStream.range(Board.FIRST_SQUARE_INDEX, Board.MAX_SQUARES)
                .mapToObj(Position::new)
                .toList();
    }

    private Position(int index) {
        this.index = index;
    }

    private boolean illegalJump(int x, Position destination) {
        return x < 0 && destination.fileIndex() > fileIndex() || x > 0 && destination.fileIndex() < fileIndex();
    }

    private int fileIndex() {
        return index % Board.SIDE_LENGTH;
    }

    private int horizontalClamp(int x) {
        return x % Board.SIDE_LENGTH;
    }
}
