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

class AttackGeneratorTest {

    @Test
    void pawnAttacks() {
        var board = FenParser.parse("4k3/8/8/1BpR4/1NPN4/8/8/4K3 b - - 0 1");

        var generator = new AttackGenerator(board, Color.BLACK, board.pieces(Color.BLACK), board.pieces(Color.WHITE));

        var pawn = board.pieceAt("c5");

        var expectedAttacks = new Move[] {
            Move.createCapture(pawn, Coordinate.of("b4"), board.pieceAt("b4")),
            Move.createCapture(pawn, Coordinate.of("d4"), board.pieceAt("d4"))
        };

        assertThat(generator.calculateAttacks(Color.BLACK)).hasSize(2).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void rookAttacks() {
        var board = FenParser.parse("4k3/p7/8/8/R3Bb2/8/8/n3K3 w - - 0 1");

        var generator = new AttackGenerator(board, Color.WHITE, board.pieces(Color.WHITE), board.pieces(Color.BLACK));

        var rook = board.pieceAt("a4");

        var expectedAttacks = new Move[] {
            Move.createCapture(rook, Coordinate.of("a7"), board.pieceAt("a7")),
            Move.createCapture(rook, Coordinate.of("a1"), board.pieceAt("a1"))
        };

        assertThat(generator.calculateAttacks(Color.WHITE)).hasSize(2).containsOnlyOnce(expectedAttacks);
    }
}
