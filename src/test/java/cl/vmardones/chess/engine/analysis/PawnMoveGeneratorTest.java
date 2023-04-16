/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.parser.FenParser;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class PawnMoveGeneratorTest {

    @Test
    void pawnJumps() {
        var board = FenParser.parse("4k3/p1p1p3/8/2q1N3/8/8/8/4K3 b - - 0 1");

        var jumper = board.pieceAt("a7");
        var generator = new PawnMoveGenerator(board, Color.BLACK, board.pieces(Color.BLACK));

        var expectedMove = Move.createPawnJump((Pawn) jumper, Position.of("a5"));

        assertThat(generator.calculatePawnMoves()).hasSize(1).containsOnlyOnce(expectedMove);
    }

    @Test
    void enPassantMoves() {
        var board = FenParser.parse("4k3/8/8/PpP2P2/8/8/8/4K3 w - b5 0 1");

        // Only the left and right pawns can en passant
        var leftPawn = (Pawn) board.pieceAt("a5");
        var rightPawn = (Pawn) board.pieceAt("c5");
        var otherPawn = (Pawn) board.pieceAt("f5");

        var generator = new PawnMoveGenerator(board, Color.WHITE, board.pieces(Color.WHITE));

        var expectedMoves = new Move[] {
            Move.createEnPassant(leftPawn, Position.of("b6"), board.enPassantPawn()),
            Move.createEnPassant(rightPawn, Position.of("b6"), board.enPassantPawn())
        };

        var unexpectedMoves = new Move[] {
            Move.createEnPassant(rightPawn, Position.of("d6"), board.enPassantPawn()),
            Move.createEnPassant(otherPawn, Position.of("e6"), board.enPassantPawn()),
            Move.createEnPassant(otherPawn, Position.of("g6"), board.enPassantPawn())
        };

        assertThat(generator.calculatePawnMoves())
                .containsOnlyOnce(expectedMoves)
                .doesNotContain(unexpectedMoves);
    }
}
