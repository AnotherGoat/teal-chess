package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

final class RookMoveGeneratorTest {

    @Test
    void normalMoves() {
        var position = FenParser.parse("4k3/3r4/8/6p1/3R2P1/8/8/4K3 w - - 0 1");
        var generator = new RookMoveGenerator(position);

        var expectedMoves = new Move[] {
                new Move(NORMAL, d4, d1),
                new Move(NORMAL, d4, b4),
                new Move(NORMAL, d4, d5),
                new Move(NORMAL, d4, d6),
                new Move(NORMAL, d4, f4),
        };

        var unexpectedMoves = new Move[] {
                new Move(NORMAL, d4, g4),
                new Move(NORMAL, d4, d7)
        };

        var captures = 1;
        assertThat(generator.generate()).hasSize(captures + 10).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }

    @Test
    void captures() {
        var position = FenParser.parse("4k3/p7/8/8/R3Bb2/8/8/n3K3 w - - 0 1");
        var generator = new RookMoveGenerator(position);

        var expectedCaptures = new Move[] {
                new Move(CAPTURE, a4, a7),
                new Move(CAPTURE, a4, a1)
        };

        var unexpectedCaptures = new Move[] {
                new Move(CAPTURE, a4, a6),
                new Move(CAPTURE, a4, d4),
                new Move(CAPTURE, a4, d5)
        };

        var normalMoves = 7;
        assertThat(generator.generate()).hasSize(normalMoves + 2).containsOnlyOnce(expectedCaptures).doesNotContain(unexpectedCaptures);
    }

    @Test
    void blockedSlidingMoves() {
        var position = FenParser.parse("4k3/5p2/QB3rpP/8/5B2/8/8/4K3 w - - 0 1");
        var generator = new RookMoveGenerator(position);

        var unexpectedMoves = new Move[] {
                new Move(NORMAL, f6, f8),
                new Move(NORMAL, f6, f2),
                new Move(CAPTURE, f6, h6),
                new Move(CAPTURE, f6, a6)
        };

        assertThat(generator.generate())
                .doesNotContain(unexpectedMoves);
    }
}