/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

final class BoardTest {

    Board initialBoard = Board.INITIAL_BOARD;
    Color white = Color.WHITE;
    Color black = Color.BLACK;

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Board.class).verify();
    }

    @Test
    void initialWhitePawns() {
        var expectedBitboard = 0b11111111_00000000L;
        assertThat(initialBoard.pawns(white)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackPawns() {
        var expectedBitboard = 0b11111111_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(initialBoard.pawns(black)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteKnights() {
        var expectedBitboard = 0b01000010L;
        assertThat(initialBoard.knights(white)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackKnights() {
        var expectedBitboard = 0b01000010_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(initialBoard.knights(black)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteBishops() {
        var expectedBitboard = 0b00100100L;
        assertThat(initialBoard.bishops(white)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackBishops() {
        var expectedBitboard = 0b00100100_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(initialBoard.bishops(black)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteRooks() {
        var expectedBitboard = 0b10000001L;
        assertThat(initialBoard.rooks(white)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackRooks() {
        var expectedBitboard = 0b10000001_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(initialBoard.rooks(black)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteQueen() {
        var expectedBitboard = 0b00001000L;
        assertThat(initialBoard.queens(white)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackQueen() {
        var expectedBitboard = 0b00001000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(initialBoard.queens(black)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteKing() {
        var expectedBitboard = 0b00010000L;
        assertThat(initialBoard.kings(white)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackKing() {
        var expectedBitboard = 0b00010000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(initialBoard.kings(black)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialEmptySquares() {
        IntStream.range(16, 48).forEach(coordinate -> assertThat(initialBoard.pieceAt(coordinate))
                .isNull());
    }

    @Test
    void initialWhitePawnRank() {
        IntStream.range(8, 16).forEach(coordinate -> assertThat(initialBoard.pieceAt(coordinate))
                .isEqualTo(new Piece(PieceType.PAWN, white, coordinate)));
    }

    @Test
    void initialBlackPawnRank() {
        IntStream.range(48, 56).forEach(coordinate -> assertThat(initialBoard.pieceAt(coordinate))
                .isEqualTo(new Piece(PieceType.PAWN, black, coordinate)));
    }

    @Test
    void initialWhiteBackRank() {
        assertThat(initialBoard.pieceAt(0)).isEqualTo(new Piece(PieceType.ROOK, white, 0));
        assertThat(initialBoard.pieceAt(1)).isEqualTo(new Piece(PieceType.KNIGHT, white, 1));
        assertThat(initialBoard.pieceAt(2)).isEqualTo(new Piece(PieceType.BISHOP, white, 2));
        assertThat(initialBoard.pieceAt(3)).isEqualTo(new Piece(PieceType.QUEEN, white, 3));
        assertThat(initialBoard.pieceAt(4)).isEqualTo(new Piece(PieceType.KING, white, 4));
        assertThat(initialBoard.pieceAt(5)).isEqualTo(new Piece(PieceType.BISHOP, white, 5));
        assertThat(initialBoard.pieceAt(6)).isEqualTo(new Piece(PieceType.KNIGHT, white, 6));
        assertThat(initialBoard.pieceAt(7)).isEqualTo(new Piece(PieceType.ROOK, white, 7));
    }

    @Test
    void initialBlackBackRank() {
        assertThat(initialBoard.pieceAt(56)).isEqualTo(new Piece(PieceType.ROOK, black, 56));
        assertThat(initialBoard.pieceAt(57)).isEqualTo(new Piece(PieceType.KNIGHT, black, 57));
        assertThat(initialBoard.pieceAt(58)).isEqualTo(new Piece(PieceType.BISHOP, black, 58));
        assertThat(initialBoard.pieceAt(59)).isEqualTo(new Piece(PieceType.QUEEN, black, 59));
        assertThat(initialBoard.pieceAt(60)).isEqualTo(new Piece(PieceType.KING, black, 60));
        assertThat(initialBoard.pieceAt(61)).isEqualTo(new Piece(PieceType.BISHOP, black, 61));
        assertThat(initialBoard.pieceAt(62)).isEqualTo(new Piece(PieceType.KNIGHT, black, 62));
        assertThat(initialBoard.pieceAt(63)).isEqualTo(new Piece(PieceType.ROOK, black, 63));
    }
}
