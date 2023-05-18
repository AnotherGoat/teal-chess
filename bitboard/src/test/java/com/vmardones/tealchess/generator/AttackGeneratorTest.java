/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.color.Color.BLACK;
import static com.vmardones.tealchess.color.Color.WHITE;
import static com.vmardones.tealchess.position.Position.INITIAL_POSITION;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

final class AttackGeneratorTest {

    AttackGenerator generator = new AttackGenerator();

    @Test
    void kingAttacks() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 0 1");
        var expectedWhiteAttacks = 0b00111000_00101000L;
        var expectedBlackAttacks = 0b00101000_00111000_00000000_00000000_00000000_00000000_00000000_00000000L;

        assertThat(generator.generate(position, WHITE)).isEqualTo(expectedWhiteAttacks);
        assertThat(generator.generate(position, BLACK)).isEqualTo(expectedBlackAttacks);
    }

    @Test
    void pawnAttacks() {
        var position = FenParser.parse("4k3/8/8/2p5/8/8/8/4K3 w - - 0 1");
        var expectedAttacks = 0b00101000_00111000_00000000_00000000_00001010_00000000_00000000_00000000L;

        assertThat(generator.generate(position, BLACK)).isEqualTo(expectedAttacks);
    }

    @Test
    void knightAttacks() {
        var position = FenParser.parse("4k3/8/8/8/6N1/8/8/4K3 b - - 0 1");
        var expectedAttacks = 0b10100000_00010000_00000000_00010000_10111000_00101000L;

        assertThat(generator.generate(position, WHITE)).isEqualTo(expectedAttacks);
    }

    @Test
    void bishopAttacks() {
        var position = FenParser.parse("4k3/8/8/8/3b4/8/8/4K3 w - - 0 1");
        var expectedAttacks = 0b10101000_01111001_00100010_00010100_00000000_00010100_00100010_01000001L;

        assertThat(generator.generate(position, BLACK)).isEqualTo(expectedAttacks);
    }

    @Test
    void rookAttacks() {
        var position = FenParser.parse("4k3/8/8/8/R7/8/8/4K3 b - - 0 1");
        var expectedAttacks = 0b00000001_00000001_00000001_00000001_11111110_00000001_00111001_00101001L;

        assertThat(generator.generate(position, WHITE)).isEqualTo(expectedAttacks);
    }

    @Test
    void queenAttacks() {
        var position = FenParser.parse("4k3/2q5/8/8/8/8/8/4K3 w - - 0 1");
        var expectedAttacks = 0b00101110_11111011_00001110_00010101_00100100_01000100_10000100_00000100L;

        assertThat(generator.generate(position, BLACK)).isEqualTo(expectedAttacks);
    }

    @Test
    void initialWhiteAttacks() {
        var expectedAttacks = 0b11111111_11111111_01111110L;
        assertThat(generator.generate(INITIAL_POSITION, WHITE)).isEqualTo(expectedAttacks);
    }

    @Test
    void initialBlackAttacks() {
        var expectedAttacks = 0b01111110_11111111_11111111_00000000_00000000_00000000_00000000_00000000L;
        assertThat(generator.generate(INITIAL_POSITION, BLACK)).isEqualTo(expectedAttacks);
    }

    @Test
    void blockedSlidingAttacks() {
        var position = FenParser.parse("4k3/p7/5B2/6r1/3n4/R5p1/r1b2Q2/3nK3 b - - 0 1");
        var expectedAttacks = 0b10001000_01010001_00100001_01110001_00101001_01111110_11111101_01111000L;

        assertThat(generator.generate(position, WHITE)).isEqualTo(expectedAttacks);
    }
}
