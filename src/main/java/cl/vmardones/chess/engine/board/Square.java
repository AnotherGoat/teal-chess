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

    private static final Map<String, Square> EMPTY_SQUARE_CACHE = fillEmptySquareCache();

    private final Position position;
    private final Alliance color;
    private final @Nullable Piece piece;

    /* Square creation */

    /**
     * Static factory method for creating a new square.
     *
     * @param position Position of the square, in algebraic notation.
     * @param piece The piece on the square.
     * @return A new square.
     */
    public static Square create(String position, @Nullable Piece piece) {
        if (piece == null) {
            return EMPTY_SQUARE_CACHE.get(position);
        }

        return new Square(position, piece);
    }

    /* Getters */

    public Position position() {
        return position;
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
        return position.equals(other.position) && color.equals(other.color) && Objects.equals(piece, other.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, color, piece);
    }

    /**
     * String representation of this square, used when displaying the board.
     *
     * @return The character that represents the piece, or - if the square is empty.
     */
    @Override
    public String toString() {
        if (piece == null) {
            return color == Alliance.WHITE ? "□" : "■";
        }

        return piece.unicodeChar();
    }

    private static Map<String, Square> fillEmptySquareCache() {
        record Entry(String position, Square square) {}

        return IntStream.range(Board.FIRST_SQUARE_INDEX, Board.MAX_SQUARES)
                .mapToObj(AlgebraicConverter::toAlgebraic)
                .map(position -> new Entry(position, new Square(position)))
                .collect(Collectors.toMap(Entry::position, Entry::square));
    }

    private Square(String position) {
        this(position, null);
    }

    private Square(String position, @Nullable Piece piece) {
        this(Position.of(position), piece);
    }

    private Square(Position position, @Nullable Piece piece) {
        this.position = position;
        this.piece = piece;
        color = assignColor();
    }

    private Alliance assignColor() {
        if ((position.index() + position.index() / Board.SIDE_LENGTH) % 2 == 0) {
            return Alliance.WHITE;
        }

        return Alliance.BLACK;
    }
}
