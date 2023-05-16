package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

final class KingMoveGeneratorTest {

    @Test
    void normalMoves() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b - - 0 1");
        var generator = new KingMoveGenerator(position);

        var expectedMoves = new Move[] {
                new Move(NORMAL, e8, d8),
                new Move(NORMAL, e8, f8),
                new Move(NORMAL, e8, d7),
                new Move(NORMAL, e8, e7),
                new Move(NORMAL, e8, f7),
        };

        assertThat(generator.generate()).hasSize(5).containsOnlyOnce(expectedMoves);
    }

    @Test
    void captures() {
        var position = FenParser.parse("3pkb2/3R1N2/8/8/8/8/8/4K3 b - - 0 1");
        var generator = new KingMoveGenerator(position);

        var expectedCaptures = new Move[] {
                new Move(CAPTURE, e8, d7),
                new Move(CAPTURE, e8, f7)
        };

        var unexpectedCaptures = new Move[] {
                new Move(CAPTURE, e8, d8),
                new Move(CAPTURE, e8, f8),
                new Move(CAPTURE, e8, e7)
        };

        // Technically this move is illegal, but move generators only generate pseudo-legal moves
        var normalMoves = 1;
        assertThat(generator.generate()).hasSize(normalMoves + 2).containsOnlyOnce(expectedCaptures).doesNotContain(unexpectedCaptures);
    }
}