/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

final class BitboardManipulatorTest {

    long zeros = 0L;
    long ones = ~zeros;

    @Test
    void singleBit() {
        assertThat(BitboardManipulator.singleBit(0)).isEqualTo(1L);
        assertThat(BitboardManipulator.singleBit(1)).isEqualTo(1L << 1);
        assertThat(BitboardManipulator.singleBit(2)).isEqualTo(1L << 2);

        assertThat(BitboardManipulator.singleBit(61)).isEqualTo(1L << 61);
        assertThat(BitboardManipulator.singleBit(62)).isEqualTo(1L << 62);
        assertThat(BitboardManipulator.singleBit(63)).isEqualTo(1L << 63);
    }

    @Test
    void setZeros() {
        assertThat(BitboardManipulator.set(zeros, 0)).isEqualTo(1L);
        assertThat(BitboardManipulator.set(zeros, 1)).isEqualTo(1L << 1);
        assertThat(BitboardManipulator.set(zeros, 2)).isEqualTo(1L << 2);

        assertThat(BitboardManipulator.set(zeros, 61)).isEqualTo(1L << 61);
        assertThat(BitboardManipulator.set(zeros, 62)).isEqualTo(1L << 62);
        assertThat(BitboardManipulator.set(zeros, 63)).isEqualTo(1L << 63);
    }

    @Test
    void setOnes() {
        IntStream.range(0, Long.SIZE).forEach(square -> {
            assertThat(BitboardManipulator.set(ones, square)).isEqualTo(ones);
        });
    }

    @Test
    void clearOnes() {
        assertThat(BitboardManipulator.clear(ones, 0)).isEqualTo(~1L);
        assertThat(BitboardManipulator.clear(ones, 1)).isEqualTo(~(1L << 1));
        assertThat(BitboardManipulator.clear(ones, 2)).isEqualTo(~(1L << 2));

        assertThat(BitboardManipulator.clear(ones, 61)).isEqualTo(~(1L << 61));
        assertThat(BitboardManipulator.clear(ones, 62)).isEqualTo(~(1L << 62));
        assertThat(BitboardManipulator.clear(ones, 63)).isEqualTo(~(1L << 63));
    }

    @Test
    void clearZeros() {
        IntStream.range(0, Long.SIZE).forEach(square -> {
            assertThat(BitboardManipulator.clear(zeros, square)).isEqualTo(zeros);
        });
    }

    @Test
    void toggle() {
        var bitboard = 0b10101;

        assertThat(BitboardManipulator.toggle(bitboard, 0)).isEqualTo(0b10100L);
        assertThat(BitboardManipulator.toggle(bitboard, 1)).isEqualTo(0b10111L);
        assertThat(BitboardManipulator.toggle(bitboard, 2)).isEqualTo(0b10001L);
        assertThat(BitboardManipulator.toggle(bitboard, 3)).isEqualTo(0b11101L);
        assertThat(BitboardManipulator.toggle(bitboard, 4)).isEqualTo(0b101L);
        assertThat(BitboardManipulator.toggle(bitboard, 5)).isEqualTo(0b110101L);
    }

    @Test
    void isSet() {
        IntStream.range(0, Long.SIZE).forEach(square -> {
            assertThat(BitboardManipulator.isSet(ones, square)).isTrue();
        });
    }

    @Test
    void isNotSet() {
        IntStream.range(0, Long.SIZE).forEach(square -> {
            assertThat(BitboardManipulator.isSet(zeros, square)).isFalse();
        });
    }

    @Test
    void firstBit() {
        var bitboard = 0b100100100L;
        assertThat(BitboardManipulator.firstBit(bitboard)).isEqualTo(2);
    }

    @Test
    void firstOnesBit() {
        assertThat(BitboardManipulator.firstBit(ones)).isEqualTo(0);
    }

    @Test
    void firstZerosBit() {
        assertThat(BitboardManipulator.firstBit(zeros)).isEqualTo(64);
    }

    @Test
    void lastBit() {
        var bitboard = 0b100100100L;
        assertThat(BitboardManipulator.lastBit(bitboard)).isEqualTo(8);
    }

    @Test
    void lastOnesBit() {
        assertThat(BitboardManipulator.lastBit(ones)).isEqualTo(63);
    }

    @Test
    void lastZerosBit() {
        assertThat(BitboardManipulator.lastBit(zeros)).isEqualTo(-1);
    }

    @Test
    void bitCount() {
        var bitboard = 0b100010100110100L;
        assertThat(BitboardManipulator.bitCount(bitboard)).isEqualTo(6);
    }

    @Test
    void onesBitCount() {
        assertThat(BitboardManipulator.bitCount(ones)).isEqualTo(64);
    }

    @Test
    void zerosBitCount() {
        assertThat(BitboardManipulator.bitCount(zeros)).isEqualTo(0);
    }

    @Test
    void zerosToString() {
        var expectedResult =
                """
        00000000
        00000000
        00000000
        00000000
        00000000
        00000000
        00000000
        00000000""";

        assertThat(BitboardManipulator.toString(zeros)).isEqualTo(expectedResult);
    }

    @Test
    void onesToString() {
        var expectedResult =
                """
        11111111
        11111111
        11111111
        11111111
        11111111
        11111111
        11111111
        11111111""";

        assertThat(BitboardManipulator.toString(ones)).isEqualTo(expectedResult);
    }

    @Test
    void lightSquaresToString() {
        var lightSquares = 0x55aa55aa55aa55aaL;
        var expectedResult =
                """
        10101010
        01010101
        10101010
        01010101
        10101010
        01010101
        10101010
        01010101""";

        assertThat(BitboardManipulator.toString(lightSquares)).isEqualTo(expectedResult);
    }

    @Test
    void fileAToString() {
        var fileA = 0x01_01_01_01_01_01_01_01L;
        var expectedResult =
                """
        10000000
        10000000
        10000000
        10000000
        10000000
        10000000
        10000000
        10000000""";

        assertThat(BitboardManipulator.toString(fileA)).isEqualTo(expectedResult);
    }

    @Test
    void fileHToString() {
        var fileH = 0x80_80_80_80_80_80_80_80L;
        var expectedResult =
                """
        00000001
        00000001
        00000001
        00000001
        00000001
        00000001
        00000001
        00000001""";

        assertThat(BitboardManipulator.toString(fileH)).isEqualTo(expectedResult);
    }

    @Test
    void mainDiagonalToString() {
        var mainDiagonal = 0x80_40_20_10_08_04_02_01L;
        var expectedResult =
                """
        00000001
        00000010
        00000100
        00001000
        00010000
        00100000
        01000000
        10000000""";

        assertThat(BitboardManipulator.toString(mainDiagonal)).isEqualTo(expectedResult);
    }

    @Test
    void halfMainDiagonalToString() {
        // Tests a non-symmetric case
        var halfMainDiagonal = 0x08_04_02_01L;
        var expectedResult =
                """
        00000000
        00000000
        00000000
        00000000
        00010000
        00100000
        01000000
        10000000""";

        assertThat(BitboardManipulator.toString(halfMainDiagonal)).isEqualTo(expectedResult);
    }
}
