/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static com.vmardones.tealchess.color.Color.*;
import static com.vmardones.tealchess.coordinate.Coordinate.*;
import static com.vmardones.tealchess.piece.PieceType.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.parser.Unicode;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The chessboard, composed of 8x8 squares.
 * Each square is associated with a coordinate and can be individually checked.
 * To allow performing fast calculations, bitboards for all the piece types can also be obtained.
 * @see <a href="https://www.chessprogramming.org/Chessboard">Chessboard</a>
 * @see <a href="https://www.chessprogramming.org/Squares">Squares</a>
 */
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

    private static final long LIGHT_SQUARES = 0x55AA55AA55AA55AAL;

    // Arrays of bitboards: https://www.chessprogramming.org/Bitboard_Board-Definition#Array
    private final long[][] bitboards;
    private final @Nullable Piece[] mailbox;
    private final Piece whiteKing;
    private final Piece blackKing;

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

    /**
     * A special builder intended to be used when players make a move. This can only be used after the
     * board has been initialized at least once. It keeps the current state of the board and lets you
     * specify only the differences from the previous position.
     *
     * @return The next position builder.
     */
    public BoardBuilder nextPositionBuilder() {
        return new BoardBuilder(this);
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
     * @see <a href="https://www.chessprogramming.org/Color_of_a_Square">Color of a Square</a>
     */
    public Color colorOf(int coordinate) {
        return BitboardManipulator.isSet(LIGHT_SQUARES, coordinate) ? WHITE : BLACK;
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

    /**
     * Directly gets the king of a specific color.
     * It's common to use this method when checking if a player is in check.
     * @param color The color of the king.
     * @return The requested king.
     */
    public Piece king(Color color) {
        return color.isWhite() ? whiteKing : blackKing;
    }

    public @Nullable Piece[] mailbox() {
        return mailbox.clone();
    }

    public long pawns(Color color) {
        return bitboards[PAWN.ordinal()][color.ordinal()];
    }

    public long knights(Color color) {
        return bitboards[KNIGHT.ordinal()][color.ordinal()];
    }

    public long bishops(Color color) {
        return bitboards[BISHOP.ordinal()][color.ordinal()];
    }

    public long rooks(Color color) {
        return bitboards[ROOK.ordinal()][color.ordinal()];
    }

    public long queens(Color color) {
        return bitboards[QUEEN.ordinal()][color.ordinal()];
    }

    public long kings(Color color) {
        return bitboards[KING.ordinal()][color.ordinal()];
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
        var builder = Board.builder(e1, e8);

        builder.with(new Piece(ROOK, WHITE, a1))
                .with(new Piece(KNIGHT, WHITE, b1))
                .with(new Piece(BISHOP, WHITE, c1))
                .with(new Piece(QUEEN, WHITE, d1))
                .with(new Piece(BISHOP, WHITE, f1))
                .with(new Piece(KNIGHT, WHITE, g1))
                .with(new Piece(ROOK, WHITE, h1));

        for (int i = 8; i < 16; i++) {
            builder.with(new Piece(PAWN, WHITE, i));
        }

        for (int i = 48; i < 56; i++) {
            builder.with(new Piece(PAWN, BLACK, i));
        }

        builder.with(new Piece(ROOK, BLACK, a8))
                .with(new Piece(KNIGHT, BLACK, b8))
                .with(new Piece(BISHOP, BLACK, c8))
                .with(new Piece(QUEEN, BLACK, d8))
                .with(new Piece(BISHOP, BLACK, f8))
                .with(new Piece(KNIGHT, BLACK, g8))
                .with(new Piece(ROOK, BLACK, h8));

        return builder.build();
    }

    private Board(BoardBuilder builder) {
        bitboards = builder.bitboards;
        mailbox = builder.mailbox;
        whiteKing = builder.whiteKing;
        blackKing = builder.blackKing;
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
        return Arrays.deepEquals(bitboards, other.bitboards)
                && Arrays.equals(mailbox, other.mailbox)
                && whiteKing.equals(other.whiteKing)
                && blackKing.equals(other.blackKing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(bitboards), Arrays.hashCode(mailbox), whiteKing, blackKing);
    }

    /**
     * Responsible for building the board, a complex object.
     * The builder shouldn't be reutilized after finishing the building process once.
     */
    public static class BoardBuilder {

        private final long[][] bitboards;
        private final @Nullable Piece[] mailbox;
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
            bitboards = new long[PieceType.values().length][Color.values().length];
            mailbox = new Piece[NUMBER_OF_SQUARES];
            whiteKing = new Piece(KING, WHITE, whiteKingCoordinate);
            blackKing = new Piece(KING, BLACK, blackKingCoordinate);
        }

        private BoardBuilder(Board board) {
            bitboards = board.bitboards.clone();
            mailbox = board.mailbox.clone();
            whiteKing = board.whiteKing;
            blackKing = board.blackKing;
        }
    }
}
