/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.move.MoveType;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.piece.Pawn;
import com.vmardones.tealchess.piece.PromotionChoice;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PawnMoveGeneratorTest {

    @Test
    void whitePawnPushes() {
        var position = FenParser.parse("4k3/8/8/8/6p1/1P4P1/8/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var board = position.board();
        var leftPawn = board.pieceAt(Coordinate.of("b3"));

        var expectedMove = Move.normal(leftPawn, Coordinate.of("b4"));

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void blackPawnPushes() {
        var position = FenParser.parse("4k3/8/1p4p1/6P1/8/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var board = position.board();
        var leftPawn = board.pieceAt(Coordinate.of("b6"));

        var expectedMove = Move.normal(leftPawn, Coordinate.of("b5"));

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void whiteDoublePushes() {
        var position = FenParser.parse("4k3/8/8/8/4Q1n1/2N5/P1P1P1P1/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var board = position.board();
        var jumper = (Pawn) board.pieceAt(Coordinate.of("a2"));

        var expectedMove = Move.doublePush(jumper, Coordinate.of("a4"));

        assertThat(generator.generate())
                .hasSize(4)
                .containsOnlyOnce(expectedMove)
                .satisfiesOnlyOnce(move -> assertThat(move.type()).isEqualTo(MoveType.DOUBLE_PUSH));
    }

    @Test
    void blackDoublePushes() {
        var position = FenParser.parse("4k3/p1p1p1p1/2n5/4q1N1/8/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var board = position.board();
        var jumper = (Pawn) board.pieceAt(Coordinate.of("a7"));

        var expectedMove = Move.doublePush(jumper, Coordinate.of("a5"));

        assertThat(generator.generate())
                .hasSize(4)
                .containsOnlyOnce(expectedMove)
                .satisfiesOnlyOnce(move -> assertThat(move.type()).isEqualTo(MoveType.DOUBLE_PUSH));
    }

    @Test
    void blockedDoublePush() {}

    @Test
    void whitePawnCaptures() {
        var position = FenParser.parse("4k3/8/8/1npn4/1bPr4/8/8/4K3 w - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var board = position.board();
        var pawn = board.pieceAt(Coordinate.of("c4"));

        var expectedCaptures = new Move[] {
            Move.capture(pawn, board.pieceAt(Coordinate.of("b5"))),
            Move.capture(pawn, board.pieceAt(Coordinate.of("d5")))
        };

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void blackPawnCaptures() {
        var position = FenParser.parse("4k3/8/8/1BpR4/1NPN4/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var board = position.board();
        var pawn = board.pieceAt(Coordinate.of("c5"));

        var expectedCaptures = new Move[] {
            Move.capture(pawn, board.pieceAt(Coordinate.of("b4"))),
            Move.capture(pawn, board.pieceAt(Coordinate.of("d4")))
        };

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void whiteEnPassantMoves() {
        var position = FenParser.parse("4k3/8/8/PpP2P2/8/8/8/4K3 w - b6 0 1");
        var generator = new PawnMoveGenerator(position);

        // Only the left and right pawns can en passant
        var board = position.board();
        var leftPawn = (Pawn) board.pieceAt(Coordinate.of("a5"));
        var rightPawn = (Pawn) board.pieceAt(Coordinate.of("c5"));
        var otherPawn = (Pawn) board.pieceAt(Coordinate.of("f5"));

        var attackedPawn = (Pawn) board.pieceAt(Coordinate.of("b5"));

        var expectedMoves = new Move[] {
            Move.enPassant(leftPawn, Coordinate.of("b6"), attackedPawn),
            Move.enPassant(rightPawn, Coordinate.of("b6"), attackedPawn)
        };

        var unexpectedMoves = new Move[] {
            Move.enPassant(rightPawn, Coordinate.of("d6"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("e6"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("g6"), attackedPawn)
        };

        assertThat(generator.generate()).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }

    @Test
    void blackEnPassantMoves() {
        var position = FenParser.parse("4k3/8/8/8/pPp2p2/8/8/4K3 b - b3 0 1");
        var generator = new PawnMoveGenerator(position);

        // Only the left and right pawns can en passant
        var board = position.board();
        var leftPawn = (Pawn) board.pieceAt(Coordinate.of("a4"));
        var rightPawn = (Pawn) board.pieceAt(Coordinate.of("c4"));
        var otherPawn = (Pawn) board.pieceAt(Coordinate.of("f4"));

        var attackedPawn = (Pawn) board.pieceAt(Coordinate.of("b4"));

        var expectedMoves = new Move[] {
            Move.enPassant(leftPawn, Coordinate.of("b3"), attackedPawn),
            Move.enPassant(rightPawn, Coordinate.of("b3"), attackedPawn)
        };

        var unexpectedMoves = new Move[] {
            Move.enPassant(rightPawn, Coordinate.of("d3"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("e3"), attackedPawn),
            Move.enPassant(otherPawn, Coordinate.of("g3"), attackedPawn)
        };

        assertThat(generator.generate()).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
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
