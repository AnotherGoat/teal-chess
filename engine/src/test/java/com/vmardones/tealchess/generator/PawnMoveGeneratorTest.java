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
import com.vmardones.tealchess.piece.PromotionChoice;
import org.junit.jupiter.api.Test;

final class PawnMoveGeneratorTest {

    MoveGenerator generator = new PawnMoveGenerator();

    @Test
    void whitePawnPushes() {
        var position = FenParser.parse("4k3/8/8/8/6p1/1P4P1/8/4K3 w - - 0 1");
        var expectedMove = new Move(PAWN_PUSH, b3, b4);

        assertThat(generator.generate(position)).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void blackPawnPushes() {
        var position = FenParser.parse("4k3/8/1p4p1/6P1/8/8/8/4K3 b - - 0 1");
        var expectedMove = new Move(PAWN_PUSH, b6, b5);

        assertThat(generator.generate(position)).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void whiteDoublePushes() {
        var position = FenParser.parse("4k3/8/8/8/4Q1n1/2N5/P1P1P1P1/4K3 w - - 0 1");
        var expectedMove = new Move(DOUBLE_PUSH, a2, a4);

        assertThat(generator.generate(position))
                .hasSize(4)
                .containsOnlyOnce(expectedMove)
                .satisfiesOnlyOnce(move -> assertThat(move.type()).isEqualTo(DOUBLE_PUSH));
    }

    @Test
    void blackDoublePushes() {
        var position = FenParser.parse("4k3/p1p1p1p1/2n5/4q1N1/8/8/8/4K3 b - - 0 1");
        var expectedMove = new Move(DOUBLE_PUSH, a7, a5);

        assertThat(generator.generate(position))
                .hasSize(4)
                .containsOnlyOnce(expectedMove)
                .satisfiesOnlyOnce(move -> assertThat(move.type()).isEqualTo(DOUBLE_PUSH));
    }

    @Test
    void whitePawnCaptures() {
        var position = FenParser.parse("4k3/8/8/1npn4/1bPr4/8/8/4K3 w - - 0 1");

        var expectedCaptures = new Move[] {new Move(PAWN_CAPTURE, c4, b5), new Move(PAWN_CAPTURE, c4, d5)};

        assertThat(generator.generate(position)).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void blackPawnCaptures() {
        var position = FenParser.parse("4k3/8/8/1BpR4/1NPN4/8/8/4K3 b - - 0 1");

        var expectedCaptures = new Move[] {new Move(PAWN_CAPTURE, c5, b4), new Move(PAWN_CAPTURE, c5, d4)};

        assertThat(generator.generate(position)).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void noWrappingWhiteCaptures() {
        var position = FenParser.parse("4k3/8/8/p6p/P6P/8/8/4K3 w - - 0 1");
        assertThat(generator.generate(position)).isEmpty();
    }

    @Test
    void noWrappingBlackCaptures() {
        var position = FenParser.parse("4k3/8/8/p6p/P6P/8/8/4K3 b - - 0 1");
        assertThat(generator.generate(position)).isEmpty();
    }

    @Test
    void whiteEnPassantMoves() {
        var position = FenParser.parse("4k3/8/8/PpP2P2/8/8/8/4K3 w - b6 0 1");

        // Only the pawns at a5 and c5 can make an en passant capture
        var expectedMoves = new Move[] {new Move(EN_PASSANT, a5, b6), new Move(EN_PASSANT, c5, b6)};

        var unexpectedMoves =
                new Move[] {new Move(EN_PASSANT, c5, d6), new Move(EN_PASSANT, f5, e6), new Move(EN_PASSANT, f5, g6)};

        assertThat(generator.generate(position)).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }

    @Test
    void blackEnPassantMoves() {
        var position = FenParser.parse("4k3/8/8/8/pPp2p2/8/8/4K3 b - b3 0 1");

        // Only the pawns at a4 and c4 can make an en passant capture
        var expectedMoves = new Move[] {new Move(EN_PASSANT, a4, b3), new Move(EN_PASSANT, c4, b3)};

        var unexpectedMoves =
                new Move[] {new Move(EN_PASSANT, c4, d3), new Move(EN_PASSANT, f4, e3), new Move(EN_PASSANT, f4, g3)};

        assertThat(generator.generate(position)).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }

    @Test
    void noWrappingWhiteEnPassant() {
        var position = FenParser.parse("4k3/8/p7/P6p/8/8/8/4K3 w - h6 0 1");
        assertThat(generator.generate(position)).isEmpty();
    }

    @Test
    void noWrappingBlackEnPassant() {
        var position = FenParser.parse("4k3/8/8/8/P6p/7P/8/4K3 b - a3 0 1");
        assertThat(generator.generate(position)).isEmpty();
    }

    @Test
    void whitePromotions() {
        var position = FenParser.parse("4k2b/6P1/8/8/8/8/8/4K3 w - - 0 1");
        assertThat(generator.generate(position)).hasSize(8).allMatch(move -> move.promotionChoice() != null);
    }

    @Test
    void blackPromotions() {
        var position = FenParser.parse("4k3/8/8/8/8/8/6p1/4K2B b - - 0 1");
        assertThat(generator.generate(position)).hasSize(8).allMatch(move -> move.promotionChoice() != null);
    }

    @Test
    void allChoicesAvailable() {
        var position = FenParser.parse("4k3/8/8/8/8/8/6p1/4K3 b - - 0 1");

        assertThat(generator.generate(position))
                .hasSize(4)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.KNIGHT)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.BISHOP)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.ROOK)
                .anyMatch(move -> move.promotionChoice() == PromotionChoice.QUEEN);
    }

    @Test
    void noWrappingWhitePromotions() {
        var position = FenParser.parse("r3k2r/P7/8/8/8/8/8/4K3 w - - 0 1");
        assertThat(generator.generate(position)).isEmpty();
    }

    @Test
    void noWrappingBlackPromotions() {
        var position = FenParser.parse("4k3/8/8/8/8/8/7p/R3K2R w - - 0 1");
        assertThat(generator.generate(position)).isEmpty();
    }
}
