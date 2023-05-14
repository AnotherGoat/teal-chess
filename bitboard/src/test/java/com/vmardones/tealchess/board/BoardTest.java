/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.color.Color;
import org.junit.jupiter.api.Test;

final class BoardTest {

    Board initialBoard = Board.INITIAL_BOARD;
    Color white = Color.WHITE;
    Color black = Color.BLACK;

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
}
