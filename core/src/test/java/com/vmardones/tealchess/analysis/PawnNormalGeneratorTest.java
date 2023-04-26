/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.FenParser;
import com.vmardones.tealchess.piece.Pawn;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PawnNormalGeneratorTest {

    @Test
    void doublePushes() {
        var position = FenParser.parse("4k3/p1p1p3/8/2q1N3/8/8/8/4K3 b - - 0 1");
        var generator = new PawnMoveGenerator(position);

        var board = position.board();
        var jumper = board.pieceAt("a7");

        var expectedMove = Move.createDoublePush((Pawn) jumper, Coordinate.of("a5"));

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void enPassantMoves() {
        var position = FenParser.parse("4k3/8/8/PpP2P2/8/8/8/4K3 w - b6 0 1");
        var generator = new PawnMoveGenerator(position);

        // Only the left and right pawns can en passant
        var board = position.board();
        var leftPawn = (Pawn) board.pieceAt("a5");
        var rightPawn = (Pawn) board.pieceAt("c5");
        var otherPawn = (Pawn) board.pieceAt("f5");

        var enPassantTarget = position.enPassantTarget();

        var expectedMoves = new Move[] {
            Move.createEnPassant(leftPawn, Coordinate.of("b6"), enPassantTarget),
            Move.createEnPassant(rightPawn, Coordinate.of("b6"), enPassantTarget)
        };

        var unexpectedMoves = new Move[] {
            Move.createEnPassant(rightPawn, Coordinate.of("d6"), enPassantTarget),
            Move.createEnPassant(otherPawn, Coordinate.of("e6"), enPassantTarget),
            Move.createEnPassant(otherPawn, Coordinate.of("g6"), enPassantTarget)
        };

        assertThat(generator.generate()).containsOnlyOnce(expectedMoves).doesNotContain(unexpectedMoves);
    }
}
