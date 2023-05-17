/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

// TODO: Test cases where wrapping is needed
final class KnightMoveGeneratorTest {

    MoveGenerator generator = new KnightMoveGenerator();

    @Test
    void normalMoves() {
        var position = FenParser.parse("4k3/1p6/1P1r4/8/2N5/4n3/1q6/4K3 w - - 0 1");

        var expectedMoves = new Move[] {
            new Move(NORMAL, c4, a3), new Move(NORMAL, c4, a5), new Move(NORMAL, c4, d2), new Move(NORMAL, c4, e5)
        };

        var unexpectedMoves = new Move[] {new Move(NORMAL, c4, b6), new Move(NORMAL, c4, b2)};

        var captures = 3;
        assertThat(generator.generate(position))
                .hasSize(captures + 4)
                .containsOnlyOnce(expectedMoves)
                .doesNotContain(unexpectedMoves);
    }

    @Test
    void captures() {
        var position = FenParser.parse("6k1/1b1B4/1B6/2n5/P3R3/2Q5/8/4K3 b - - 0 1");

        var expectedCaptures =
                new Move[] {new Move(CAPTURE, c5, a4), new Move(CAPTURE, c5, e4), new Move(CAPTURE, c5, d7)};

        var unexpectedCaptures = new Move[] {new Move(CAPTURE, c5, b7), new Move(CAPTURE, c5, e6)};

        var normalMoves = 4;
        assertThat(generator.generate(position))
                .hasSize(normalMoves + 3)
                .containsOnlyOnce(expectedCaptures)
                .doesNotContain(unexpectedCaptures);
    }
}
