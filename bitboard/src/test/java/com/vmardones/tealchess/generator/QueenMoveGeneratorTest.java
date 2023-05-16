package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

class QueenMoveGeneratorTest {

    @Test
    void normalMoves() {
        var position = FenParser.parse("4k3/1N6/8/R2q2p1/6P1/1p1B4/1P6/4K3 b - - 0 1");
        var generator = new QueenMoveGenerator(position);

        var expectedMoves = new Move[] {
                new Move(NORMAL, d5, b5),
                new Move(NORMAL, d5, c6),
                new Move(NORMAL, d5, d8),
                new Move(NORMAL, d5, f7),
                new Move(NORMAL, d5, f5),
                new Move(NORMAL, d5, h1),
                new Move(NORMAL, d5, d4),
                new Move(NORMAL, d5, c4),
                new Move(NORMAL, d5, f3)
        };

        var unexpectedMoves = new Move[] {
                new Move(NORMAL, d5, d3),
                new Move(NORMAL, d5, b3),
                new Move(NORMAL, d5, b7),
                new Move(NORMAL, d5, g5)
        };

        var captures = 3;
        assertThat(generator.generate()).hasSize(captures + 17).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }

    @Test
    void captures() {
        var position = FenParser.parse("4k3/1p6/2R5/n2Q2N1/3N4/1P1b4/q5b1/4K3 w - - 0 1");
        var generator = new QueenMoveGenerator(position);

        var expectedCaptures = new Move[] {
                new Move(CAPTURE, d5, a5),
                new Move(CAPTURE, d5, g2)
        };

        var unexpectedCaptures = new Move[] {
                new Move(CAPTURE, d5, b3),
                new Move(CAPTURE, d5, g5),
                new Move(CAPTURE, d5, d8),
                new Move(CAPTURE, d5, g8),
                new Move(CAPTURE, d5, f3)
        };

        var normalMoves = 13;
        assertThat(generator.generate()).hasSize(normalMoves + 2).containsOnlyOnce(expectedCaptures).doesNotContain(unexpectedCaptures);
    }

    @Test
    void blockedSlidingMoves() {
        var position = FenParser.parse("4k3/1r3q2/2R2p2/8/6p1/b1n2Qn1/4PNB1/3bK3 w - - 0 1");
        var generator = new QueenMoveGenerator(position);

        var unexpectedMoves = new Move[] {
                new Move(NORMAL, f3, f1),
                new Move(NORMAL, f3, h3),
                new Move(CAPTURE, f3, a3),
                new Move(CAPTURE, f3, f7),
                new Move(NORMAL, f3, h1),
                new Move(NORMAL, f3, h5),
                new Move(CAPTURE, f3, d1),
                new Move(CAPTURE, f3, b7)
        };

        assertThat(generator.generate())
                .doesNotContain(unexpectedMoves);
    }
}