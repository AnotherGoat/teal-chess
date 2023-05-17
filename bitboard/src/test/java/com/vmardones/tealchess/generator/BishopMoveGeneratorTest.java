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

final class BishopMoveGeneratorTest {

    MoveGenerator generator = new BishopMoveGenerator();

    @Test
    void normalMoves() {
        var position = FenParser.parse("4k3/6B1/1p6/1P6/3b4/8/8/4K3 b - - 0 1");

        var expectedMoves = new Move[] {
            new Move(NORMAL, d4, c5),
            new Move(NORMAL, d4, a1),
            new Move(NORMAL, d4, b2),
            new Move(NORMAL, d4, g1),
            new Move(NORMAL, d4, f6)
        };

        var unexpectedMoves = new Move[] {new Move(NORMAL, d4, g7), new Move(NORMAL, d4, b6)};

        var captures = 1;
        assertThat(generator.generate(position))
                .hasSize(captures + 9)
                .containsOnlyOnce(expectedMoves)
                .doesNotContain(unexpectedMoves);
    }

    @Test
    void captures() {
        var position = FenParser.parse("4k3/b5r1/8/4N3/3B4/4p3/1p6/4K3 w - - 0 1");

        var expectedCaptures =
                new Move[] {new Move(CAPTURE, d4, a7), new Move(CAPTURE, d4, b2), new Move(CAPTURE, d4, e3)};

        var unexpectedCaptures = new Move[] {new Move(CAPTURE, d4, e5), new Move(CAPTURE, d4, c3)};

        var normalMoves = 3;
        assertThat(generator.generate(position))
                .hasSize(normalMoves + 3)
                .containsOnlyOnce(expectedCaptures)
                .doesNotContain(unexpectedCaptures);
    }

    @Test
    void blockedSlidingMoves() {
        var position = FenParser.parse("4k3/8/2R3n1/8/4b3/3q1P2/2B3P1/4K3 b - - 0 1");

        var unexpectedMoves = new Move[] {
            new Move(NORMAL, e4, h7), new Move(NORMAL, e4, a8), new Move(CAPTURE, e4, c2), new Move(CAPTURE, e4, g2)
        };

        assertThat(generator.generate(position)).doesNotContain(unexpectedMoves);
    }
}
