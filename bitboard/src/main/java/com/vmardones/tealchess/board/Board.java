/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import java.util.List;

import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;
import com.vmardones.tealchess.side.Side;
import org.eclipse.jdt.annotation.Nullable;

public final class Board {

    /**
     * The number of squares in the game board.
     */
    public static final int NUMBER_OF_SQUARES = 64;

    // Arrays of bitboards: https://www.chessprogramming.org/Bitboard_Board-Definition#Array
    private final long[][] bitboards;
    private final @Nullable Piece[] mailbox;

    /* Building the board */

    /**
     * The board is a complex object, so this builder is the standard method to create it. Both white
     * and black kings must be supplied for the board to be legal. This builder is intended for
     * creating a new board from scratch.
     *
     * @param whiteKing The white player's king coordinate.
     * @param blackKing The black player's king coordinate.
     * @return The board builder.
     */
    public static BoardBuilder builder(int whiteKing, int blackKing) {
        return new BoardBuilder(whiteKing, blackKing);
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

    /* Getters */

    public long pawns(Side side) {
        return bitboards[PieceType.PAWN.ordinal()][side.ordinal()];
    }

    public long knights(Side side) {
        return bitboards[PieceType.KNIGHT.ordinal()][side.ordinal()];
    }

    public long bishops(Side side) {
        return bitboards[PieceType.BISHOP.ordinal()][side.ordinal()];
    }

    public long rooks(Side side) {
        return bitboards[PieceType.ROOK.ordinal()][side.ordinal()];
    }

    public long queens(Side side) {
        return bitboards[PieceType.QUEEN.ordinal()][side.ordinal()];
    }

    public long kings(Side side) {
        return bitboards[PieceType.KING.ordinal()][side.ordinal()];
    }

    private Board(BoardBuilder boardBuilder) {
        bitboards = new long[][] {};
        mailbox = boardBuilder.mailbox;
    }

    public static class BoardBuilder {

        private final long[][] bitboards = new long[PieceType.values().length][Side.values().length];
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
            var sideIndex = piece.side().ordinal();

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
            var sideIndex = piece.side().ordinal();

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
            whiteKing = new Piece(PieceType.KING, Side.WHITE, whiteKingCoordinate);
            blackKing = new Piece(PieceType.KING, Side.BLACK, blackKingCoordinate);
        }
    }
}
