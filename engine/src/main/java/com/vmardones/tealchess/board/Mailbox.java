/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static com.vmardones.tealchess.board.BitboardManipulator.*;
import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.List;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;
import com.vmardones.tealchess.square.Coordinate;
import org.jspecify.annotations.Nullable;

public class Mailbox {

    public static final Mailbox INITIAL_MAILBOX = new Mailbox(Board.INITIAL_BOARD);
    private final List<@Nullable Piece> pieces;

    public Mailbox(Board board) {
        pieces = loadMailbox(board.bitboards());
    }

    /* Checking the board */

    /**
     * Get the piece located at a specific coordinate.
     *
     * @param coordinate The coordinate to search.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(Coordinate coordinate) {
        return pieceAt(coordinate.squareIndex());
    }

    /**
     * Get the piece located at a specific square.
     *
     * @param square The square to search.
     * @return The piece found.
     */
    public @Nullable Piece pieceAt(int square) {
        return pieces.get(square);
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

    /* Getters */

    public List<@Nullable Piece> pieces() {
        return unmodifiableList(pieces);
    }

    private List<Piece> loadMailbox(long[][] bitboards) {
        var arrayMailbox = new Piece[Board.NUMBER_OF_SQUARES];

        for (var pieceType : PieceType.values()) {
            for (var color : Color.values()) {

                var bitboard = bitboards[pieceType.ordinal()][color.ordinal()];

                if (bitboard == 0) {
                    continue;
                }

                var nextBit = firstBit(bitboard);

                do {
                    arrayMailbox[nextBit] = new Piece(pieceType, color, nextBit);

                    bitboard = clear(bitboard, nextBit);
                    nextBit = firstBit(bitboard);
                } while (isSet(bitboard, nextBit));
            }
        }

        return Arrays.asList(arrayMailbox);
    }
}
