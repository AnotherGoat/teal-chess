/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static com.vmardones.tealchess.color.Color.*;
import static com.vmardones.tealchess.piece.PieceType.*;
import static com.vmardones.tealchess.square.Square.*;

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
 * Each square can be individually checked to find the piece it could have.
 * To allow performing fast calculations, bitboards for all the piece types can also be obtained.
 * @see <a href="https://www.chessprogramming.org/Chessboard">Chessboard</a>
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

    private static final long LIGHT_SQUARES = 0x55_aa_55_aa_55_aa_55_aaL;

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
     * @param whiteKingSquare The white player's king square.
     * @param blackKingSquare The black player's king square.
     * @return The board builder.
     */
    public static BoardBuilder builder(int whiteKingSquare, int blackKingSquare) {
        return new BoardBuilder(whiteKingSquare, blackKingSquare);
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
     * Get the piece located at a specific square.
     *
     * @param square The square to search.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(int square) {
        return mailbox[square];
    }

    /**
     * Check if a specific square is empty.
     *
     * @param square The square of the square to check.
     * @return True if the square doesn't have a piece.
     */
    public boolean isEmpty(int square) {
        return pieceAt(square) == null;
    }

    /**
     * Find the color of the square at a specific square.
     * Mostly used to draw the board.
     * @param square The square of the square.
     * @return The color of the square.
     * @see <a href="https://www.chessprogramming.org/Color_of_a_Square">Color of a Square</a>
     */
    public Color colorOf(int square) {
        return BitboardManipulator.isSet(LIGHT_SQUARES, square) ? WHITE : BLACK;
    }

    /**
     * Represent a square using Unicode characters.
     * This returns the piece's Unicode representation or a white/black square character for empty squares.
     * @param square The square of the square.
     * @return Unicode representation of the square.
     */
    public String squareAsUnicode(int square) {
        var piece = pieceAt(square);

        if (piece == null) {
            return colorOf(square).unicode();
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

    // TODO: Generate the mailbox lazily, when this method is called for the first time
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

    /* Special bitboards */

    public long emptySquares() {
        return ~(pawns(WHITE)
                | pawns(BLACK)
                | knights(WHITE)
                | knights(BLACK)
                | bishops(WHITE)
                | bishops(BLACK)
                | rooks(WHITE)
                | rooks(BLACK)
                | queens(WHITE)
                | queens(BLACK)
                | kings(WHITE)
                | kings(BLACK));
    }

    public long capturablePieces(Color attacker) {
        var opposite = attacker.opposite();
        return pawns(opposite) | knights(opposite) | bishops(opposite) | rooks(opposite) | queens(opposite);
    }

    public long notCapturablePieces(Color attacker) {
        var opposite = attacker.opposite();
        return pawns(attacker)
                | knights(attacker)
                | bishops(attacker)
                | rooks(attacker)
                | queens(attacker)
                | kings(attacker)
                | kings(opposite);
    }

    @Override
    public String unicode() {
        var result = new StringBuilder();

        for (var i = 0; i < NUMBER_OF_SQUARES; i++) {
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

        for (var i = 8; i < 16; i++) {
            builder.with(new Piece(PAWN, WHITE, i));
        }

        for (var i = 48; i < 56; i++) {
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
            var square = piece.square();

            bitboards[typeIndex][sideIndex] = BitboardManipulator.set(bitboard, square);
            mailbox[square] = piece;
            return this;
        }

        /**
         * Add multiple pieces to the board. Null pieces are silently ignored.
         * If multiple pieces are put in the same square, the last one takes precedence.
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
         * @param square The square to remove a piece from.
         * @return The same instance of this builder, to continue the building process.
         */
        public BoardBuilder without(int square) {
            var piece = mailbox[square];

            if (piece == null) {
                return this;
            }

            var typeIndex = piece.type().ordinal();
            var sideIndex = piece.color().ordinal();

            var bitboard = bitboards[typeIndex][sideIndex];

            bitboards[typeIndex][sideIndex] = BitboardManipulator.clear(bitboard, square);
            mailbox[square] = null;
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

        private BoardBuilder(int whiteKingSquare, int blackKingSquare) {
            bitboards = new long[PieceType.values().length][Color.values().length];
            mailbox = new Piece[NUMBER_OF_SQUARES];
            whiteKing = new Piece(KING, WHITE, whiteKingSquare);
            blackKing = new Piece(KING, BLACK, blackKingSquare);
        }

        private BoardBuilder(Board board) {
            bitboards = board.bitboards.clone();
            mailbox = board.mailbox.clone();
            whiteKing = board.whiteKing;
            blackKing = board.blackKing;
        }
    }
}
