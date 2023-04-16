/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.parser.FenParser;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class MoveGeneratorTest {

    @Test
    void pawnMoves() {
        var board = FenParser.parse("4k3/8/8/8/6p1/1P4P1/8/4K3 w - - 0 1");

        var generator = new MoveGenerator(board, board.pieces(Color.WHITE));
        var leftPawn = board.pieceAt("b3");

        var expectedMove = Move.createNormal(leftPawn, Coordinate.of("b4"));

        assertThat(generator.calculateMoves()).containsOnlyOnce(expectedMove);
    }

    @Test
    void knightMoves() {}

    @Test
    void bishopMoves() {}

    @Test
    void rookMoves() {}

    @Test
    void queenMoves() {}

    @Test
    void kingMoves() {
        var board = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b - - 0 1");

        var generator = new MoveGenerator(board, board.pieces(Color.BLACK));
        var king = board.king(Color.BLACK);

        var expectedMoves = new Move[] {
            Move.createNormal(king, Coordinate.of("d8")),
            Move.createNormal(king, Coordinate.of("f8")),
            Move.createNormal(king, Coordinate.of("d7")),
            Move.createNormal(king, Coordinate.of("e7")),
            Move.createNormal(king, Coordinate.of("f7"))
        };

        assertThat(generator.calculateMoves()).hasSize(5).containsOnlyOnce(expectedMoves);
    }

    @Test
    void blockedSlidingMoves() {
        var board = FenParser.parse("4k3/P7/5B2/8/3N4/8/5Q2/R3K3 w - - 0 1");

        var generator = new MoveGenerator(board, board.pieces(Color.WHITE));

        var bishop = board.pieceAt("f6");
        var rook = board.pieceAt("a1");
        var queen = board.pieceAt("f2");

        var unexpectedMoves = new Move[] {
            Move.createNormal(bishop, Coordinate.of("d4")),
            Move.createNormal(bishop, Coordinate.of("c3")),
            Move.createNormal(rook, Coordinate.of("a7")),
            Move.createNormal(rook, Coordinate.of("a8")),
            Move.createNormal(queen, Coordinate.of("d4")),
            Move.createNormal(queen, Coordinate.of("c5")),
            Move.createNormal(queen, Coordinate.of("f6")),
            Move.createNormal(queen, Coordinate.of("f7"))
        };

        assertThat(generator.calculateMoves()).isNotEmpty().doesNotContain(unexpectedMoves);
    }
}
