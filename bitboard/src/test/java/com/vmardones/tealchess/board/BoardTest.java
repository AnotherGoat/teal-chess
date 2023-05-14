/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static com.vmardones.tealchess.coordinate.Coordinate.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import com.vmardones.tealchess.color.Color;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.piece.PieceType;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

final class BoardTest {

    Board initialBoard = Board.INITIAL_BOARD;
    Board board = Board.builder(4, 60).build();
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
        assertThat(initialBoard.pieceAt(a1)).isEqualTo(new Piece(PieceType.ROOK, white, a1));
        assertThat(initialBoard.pieceAt(b1)).isEqualTo(new Piece(PieceType.KNIGHT, white, b1));
        assertThat(initialBoard.pieceAt(c1)).isEqualTo(new Piece(PieceType.BISHOP, white, c1));
        assertThat(initialBoard.pieceAt(d1)).isEqualTo(new Piece(PieceType.QUEEN, white, d1));
        assertThat(initialBoard.pieceAt(e1)).isEqualTo(new Piece(PieceType.KING, white, e1));
        assertThat(initialBoard.pieceAt(f1)).isEqualTo(new Piece(PieceType.BISHOP, white, f1));
        assertThat(initialBoard.pieceAt(g1)).isEqualTo(new Piece(PieceType.KNIGHT, white, g1));
        assertThat(initialBoard.pieceAt(h1)).isEqualTo(new Piece(PieceType.ROOK, white, h1));
    }

    @Test
    void initialBlackBackRank() {
        assertThat(initialBoard.pieceAt(a8)).isEqualTo(new Piece(PieceType.ROOK, black, a8));
        assertThat(initialBoard.pieceAt(b8)).isEqualTo(new Piece(PieceType.KNIGHT, black, b8));
        assertThat(initialBoard.pieceAt(c8)).isEqualTo(new Piece(PieceType.BISHOP, black, c8));
        assertThat(initialBoard.pieceAt(d8)).isEqualTo(new Piece(PieceType.QUEEN, black, d8));
        assertThat(initialBoard.pieceAt(e8)).isEqualTo(new Piece(PieceType.KING, black, e8));
        assertThat(initialBoard.pieceAt(f8)).isEqualTo(new Piece(PieceType.BISHOP, black, f8));
        assertThat(initialBoard.pieceAt(g8)).isEqualTo(new Piece(PieceType.KNIGHT, black, g8));
        assertThat(initialBoard.pieceAt(h8)).isEqualTo(new Piece(PieceType.ROOK, black, h8));
    }

    @Test
    void isEmpty() {
        assertThat(board.isEmpty(a1)).isTrue();
        assertThat(board.isEmpty(h8)).isTrue();
    }
}
