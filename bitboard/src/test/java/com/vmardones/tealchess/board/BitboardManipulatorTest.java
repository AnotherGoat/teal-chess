/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class BitboardManipulatorTest {

    @Test
    void setZero() {
        var zero = 0L;

        assertThat(BitboardManipulator.set(zero, 0)).isEqualTo(0b1L);
        assertThat(BitboardManipulator.set(zero, 1)).isEqualTo(0b10L);
        assertThat(BitboardManipulator.set(zero, 2)).isEqualTo(0b100L);
        assertThat(BitboardManipulator.set(zero, 3)).isEqualTo(0b1000L);
        assertThat(BitboardManipulator.set(zero, 4)).isEqualTo(0b10000L);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void setOne(int coordinate) {
        var ones = 0b11111L;
        assertThat(BitboardManipulator.set(ones, coordinate)).isEqualTo(ones);
    }

    @Test
    void clearOne() {
        var ones = 0b11111L;

        assertThat(BitboardManipulator.clear(ones, 0)).isEqualTo(0b11110L);
        assertThat(BitboardManipulator.clear(ones, 1)).isEqualTo(0b11101L);
        assertThat(BitboardManipulator.clear(ones, 2)).isEqualTo(0b11011L);
        assertThat(BitboardManipulator.clear(ones, 3)).isEqualTo(0b10111L);
        assertThat(BitboardManipulator.clear(ones, 4)).isEqualTo(0b1111L);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void clearZero(int coordinate) {
        var zero = 0L;
        assertThat(BitboardManipulator.clear(zero, coordinate)).isEqualTo(zero);
    }

    @Test
    void toggle() {
        var bitboard = 0b10101;

        assertThat(BitboardManipulator.toggle(bitboard, 0)).isEqualTo(0b10100L);
        assertThat(BitboardManipulator.toggle(bitboard, 1)).isEqualTo(0b10111L);
        assertThat(BitboardManipulator.toggle(bitboard, 2)).isEqualTo(0b10001L);
        assertThat(BitboardManipulator.toggle(bitboard, 3)).isEqualTo(0b11101L);
        assertThat(BitboardManipulator.toggle(bitboard, 4)).isEqualTo(0b101L);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void isSet(int coordinate) {
        var ones = 0b11111L;
        assertThat(BitboardManipulator.isSet(ones, coordinate)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void isNotSet(int coordinate) {
        var zero = 0L;
        assertThat(BitboardManipulator.isSet(zero, coordinate)).isFalse();
    }
}
