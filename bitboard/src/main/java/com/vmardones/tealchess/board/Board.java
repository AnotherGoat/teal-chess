/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.coordinate.Coordinate;
import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;
import org.eclipse.jdt.annotation.Nullable;

public final class Board implements Unicode {

    /** The chessboard is a square grid composed of squares. This is the number of squares per side. */
    public static final int SIDE_LENGTH = 8;

    /**
     * The number of squares in the game board.
     */
    public static final int NUMBER_OF_SQUARES = SIDE_LENGTH * SIDE_LENGTH;

    /**
     * The initial position of the chess pieces in the board, which consists of a rank filled with 8 pawns on each side with a
     * formation of 8 major pieces behind.
     * Always used when starting a new game.
     * @see <a href="https://www.chessprogramming.org/Initial_Position">Initial Position</a>
     */
    public static final Board INITIAL_BOARD = createInitialBoard();

    // Arrays of bitboards: https://www.chessprogramming.org/Bitboard_Board-Definition#Array
    private final long[][] bitboards;
    private final @Nullable Piece[] mailbox;

    /* Building the board */

    /**
     * The board is a complex object, so this builder is the standard method to create it. Both white
     * and black kings must be supplied for the board to be legal. This builder is intended for
     * creating a new board from scratch.
     *
     * @param whiteKingCoordinate The white player's king coordinate.
     * @param blackKingCoordinate The black player's king coordinate.
     * @return The board builder.
     */
    public static BoardBuilder builder(int whiteKingCoordinate, int blackKingCoordinate) {
        return new BoardBuilder(whiteKingCoordinate, blackKingCoordinate);
    }

    /* Checking the board */

    /**
     * Get the piece located at a specific coordinate.
     *
     * @param coordinate The coordinate to search.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(int coordinate) {
        return mailbox[coordinate];
    }

    /**
     * Check if a specific square is empty.
     *
     * @param coordinate The coordinate of the square to check.
     * @return True if the square doesn't have a piece.
     */
    public boolean isEmpty(int coordinate) {
        return pieceAt(coordinate) == null;
    }

    /**
     * Find the color of the square at a specific coordinate.
     * Mostly used to draw the board.
     * @param coordinate The coordinate of the square.
     * @return The color of the square.
     */
    public Color colorOf(int coordinate) {
        if ((coordinate + coordinate / SIDE_LENGTH) % 2 == 0) {
            return Color.WHITE;
        }

        return Color.BLACK;
    }

    /**
     * Represent a square using Unicode characters.
     * This returns the piece's Unicode representation or a white/black square character for empty squares.
     * @param coordinate The coordinate of the square.
     * @return Unicode representation of the square.
     */
    public String squareAsUnicode(int coordinate) {
        var piece = pieceAt(coordinate);

        if (piece == null) {
            return colorOf(coordinate).unicode();
        }

        return piece.unicode();
    }

    /* Getters */

    public long pawns(Color color) {
        return bitboards[PieceType.PAWN.ordinal()][color.ordinal()];
    }

    public long knights(Color color) {
        return bitboards[PieceType.KNIGHT.ordinal()][color.ordinal()];
    }

    public long bishops(Color color) {
        return bitboards[PieceType.BISHOP.ordinal()][color.ordinal()];
    }

    public long rooks(Color color) {
        return bitboards[PieceType.ROOK.ordinal()][color.ordinal()];
    }

    public long queens(Color color) {
        return bitboards[PieceType.QUEEN.ordinal()][color.ordinal()];
    }

    public long kings(Color color) {
        return bitboards[PieceType.KING.ordinal()][color.ordinal()];
    }

    @Override
    public String unicode() {
        var result = new StringBuilder();

        for (int i = 0; i < NUMBER_OF_SQUARES; i++) {
            result.append(squareAsUnicode(i)).append(" ");

            if ((i + 1) % SIDE_LENGTH == 0) {
                result.deleteCharAt(result.length() - 1).append("\n");
            }
        }

        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    private static Board createInitialBoard() {
        var builder = Board.builder(Coordinate.e1, Coordinate.e8);

        builder.with(new Piece(PieceType.ROOK, Color.WHITE, Coordinate.a1))
                .with(new Piece(PieceType.KNIGHT, Color.WHITE, Coordinate.b1))
                .with(new Piece(PieceType.BISHOP, Color.WHITE, Coordinate.c1))
                .with(new Piece(PieceType.QUEEN, Color.WHITE, Coordinate.d1))
                .with(new Piece(PieceType.BISHOP, Color.WHITE, Coordinate.f1))
                .with(new Piece(PieceType.KNIGHT, Color.WHITE, Coordinate.g1))
                .with(new Piece(PieceType.ROOK, Color.WHITE, Coordinate.h1));

        for (int i = 8; i < 16; i++) {
            builder.with(new Piece(PieceType.PAWN, Color.WHITE, i));
        }

        for (int i = 48; i < 56; i++) {
            builder.with(new Piece(PieceType.PAWN, Color.BLACK, i));
        }

        builder.with(new Piece(PieceType.ROOK, Color.BLACK, Coordinate.a8))
                .with(new Piece(PieceType.KNIGHT, Color.BLACK, Coordinate.b8))
                .with(new Piece(PieceType.BISHOP, Color.BLACK, Coordinate.c8))
                .with(new Piece(PieceType.QUEEN, Color.BLACK, Coordinate.d8))
                .with(new Piece(PieceType.BISHOP, Color.BLACK, Coordinate.f8))
                .with(new Piece(PieceType.KNIGHT, Color.BLACK, Coordinate.g8))
                .with(new Piece(PieceType.ROOK, Color.BLACK, Coordinate.h8));

        return builder.build();
    }

    private Board(BoardBuilder builder) {
        bitboards = builder.bitboards;
        mailbox = builder.mailbox;
    }

    /* equals and hashCode */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (Board) o;
        return Arrays.deepEquals(bitboards, other.bitboards) && Arrays.equals(mailbox, other.mailbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(bitboards), Arrays.hashCode(mailbox));
    }

    public static class BoardBuilder {

        private final long[][] bitboards = new long[PieceType.values().length][Color.values().length];
        private final @Nullable Piece[] mailbox = new Piece[NUMBER_OF_SQUARES];
        private final Piece whiteKing;
        private final Piece blackKing;

        /**
         * Add a piece to the board. This action is silently ignored if the piece is null. If multiple
         * pieces are put in the same place, the last one takes precedence. Both the white and black
         * kings are added automatically when the build finishes.
         *
         * @param piece The piece to add.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder with(@Nullable Piece piece) {
            if (piece == null) {
                return this;
            }

            var typeIndex = piece.type().ordinal();
            var sideIndex = piece.color().ordinal();

            var bitboard = bitboards[typeIndex][sideIndex];
            var coordinate = piece.coordinate();

            bitboards[typeIndex][sideIndex] = BitboardManipulator.set(bitboard, coordinate);
            mailbox[coordinate] = piece;
            return this;
        }

        /**
         * Add multiple pieces to the board. Null pieces are silently ignored.
         * If multiple pieces are put in the same coordinate, the last one takes precedence.
         * @param pieces The pieces to add.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder withAll(List<@Nullable Piece> pieces) {
            pieces.forEach(this::with);
            return this;
        }

        /**
         * Remove a piece from the board.
         *
         * @param coordinate The coordinate to remove a piece from.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder without(int coordinate) {
            var piece = mailbox[coordinate];

            if (piece == null) {
                return this;
            }

            var typeIndex = piece.type().ordinal();
            var sideIndex = piece.color().ordinal();

            var bitboard = bitboards[typeIndex][sideIndex];

            bitboards[typeIndex][sideIndex] = BitboardManipulator.clear(bitboard, coordinate);
            mailbox[coordinate] = null;
            return this;
        }

        /**
         * Finish the building process of the board.
         *
         * @return The finished, unmodifiable board.
         */
        public Board build() {
            with(whiteKing);
            with(blackKing);
            return new Board(this);
        }

        private BoardBuilder(int whiteKingCoordinate, int blackKingCoordinate) {
            whiteKing = new Piece(PieceType.KING, Color.WHITE, whiteKingCoordinate);
            blackKing = new Piece(PieceType.KING, Color.BLACK, blackKingCoordinate);
        }
    }
}
