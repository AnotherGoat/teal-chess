/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.coordinate.Coordinate.*;
import static com.vmardones.tealchess.move.MoveType.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.PromotionChoice;
import org.junit.jupiter.api.Test;

final class PawnMoveGeneratorTest {

    @Test
    void whitePawnPushes() {
        var position = FenParser.parse("4k3/8/8/8/6p1/1P4P1/8/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);
        var expectedMove = new Move(PAWN_PUSH, b3, b4);

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void blackPawnPushes() {
        var position = FenParser.parse("4k3/8/1p4p1/6P1/8/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);
        var expectedMove = new Move(PAWN_PUSH, b6, b5);

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void whiteDoublePushes() {
        var position = FenParser.parse("4k3/8/8/8/4Q1n1/2N5/P1P1P1P1/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);
        var expectedMove = new Move(DOUBLE_PUSH, a2, a4);

        assertThat(generator.generate())
                .hasSize(4)
                .containsOnlyOnce(expectedMove)
                .satisfiesOnlyOnce(move -> assertThat(move.type()).isEqualTo(DOUBLE_PUSH));
    }

    @Test
    void blackDoublePushes() {
        var position = FenParser.parse("4k3/p1p1p1p1/2n5/4q1N1/8/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);
        var expectedMove = new Move(DOUBLE_PUSH, a7, a5);

        assertThat(generator.generate())
                .hasSize(4)
                .containsOnlyOnce(expectedMove)
                .satisfiesOnlyOnce(move -> assertThat(move.type()).isEqualTo(DOUBLE_PUSH));
    }

    @Test
    void whitePawnCaptures() {
        var position = FenParser.parse("4k3/8/8/1npn4/1bPr4/8/8/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var expectedCaptures = new Move[] {new Move(PAWN_CAPTURE, c4, b5), new Move(PAWN_CAPTURE, c4, d5)};

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void blackPawnCaptures() {
        var position = FenParser.parse("4k3/8/8/1BpR4/1NPN4/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var expectedCaptures = new Move[] {new Move(PAWN_CAPTURE, c5, b4), new Move(PAWN_CAPTURE, c5, d4)};

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void noWrappingWhiteCaptures() {
        var position = FenParser.parse("4k3/8/8/p6p/P6P/8/8/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void noWrappingBlackCaptures() {
        var position = FenParser.parse("4k3/8/8/p6p/P6P/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void whitePromotions() {
        var position = FenParser.parse("4k2b/6P1/8/8/8/8/8/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);

        assertThat(generator.generate()).hasSize(8).allMatch(move -> move.promotionChoice() != null);
    }

    @Test
    void blackPromotions() {
        var position = FenParser.parse("4k3/8/8/8/8/8/6p1/4K2B b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        assertThat(generator.generate()).hasSize(8).allMatch(move -> move.promotionChoice() != null);
    }

    @Test
    void allChoicesAvailable() {
        var position = FenParser.parse("4k3/8/8/8/8/8/6p1/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        assertThat(generator.generate())
                .hasSize(4)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.KNIGHT)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.BISHOP)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.ROOK)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.QUEEN);
    }
}
