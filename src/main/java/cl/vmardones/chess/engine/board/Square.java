/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import cl.vmardones.chess.engine.piece.Piece;
import org.eclipse.jdt.annotation.Nullable;

/** A single chess square, which may or may not contain a piece. */
public final class Square {

    private static final List<Square> CACHED_EMPTY_SQUARES = createEmptySquareCache();

    private final Coordinate coordinate;
    private final @Nullable Piece piece;

    /* Square creation */

    /**
     * Static factory method for creating a new square.
     *
     * @param coordinate The square's coordinate.
     * @param piece The piece on the square.
     * @return A new square.
     */
    public static Square create(Coordinate coordinate, @Nullable Piece piece) {
        return piece != null ? new Square(coordinate, piece) : CACHED_EMPTY_SQUARES.get(coordinate.index());
    }

    /* Getters */

    public Coordinate coordinate() {
        return coordinate;
    }

    public @Nullable Piece piece() {
        return piece;
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
        return coordinate.equals(other.coordinate) && Objects.equals(piece, other.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, piece);
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

    private static List<Square> createEmptySquareCache() {
        return IntStream.range(Board.MIN_SQUARES, Board.MAX_SQUARES)
                .mapToObj(Coordinate::of)
                .map(Square::new)
                .toList();
    }

    private Square(Coordinate coordinate, @Nullable Piece piece) {
        this.coordinate = coordinate;
        this.piece = piece;
    }

    private Square(Coordinate coordinate) {
        this(coordinate, null);
    }
}
