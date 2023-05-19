/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class LegalityTesterTest {

    @Test
    void removeSelfChecks() {
        var position = FenParser.parse("R1n1k3/8/2b1r3/8/B7/8/4Q3/4K3 b - - 0 1");
        var legalityTester = new LegalityTester(position);

        var board = position.board();
        var knight = board.pieceAt(Coordinate.of("c8"));
        var bishop = board.pieceAt(Coordinate.of("c6"));
        var rook = board.pieceAt(Coordinate.of("e6"));
        var enemyRook = board.pieceAt(Coordinate.of("a8"));

        var illegalMoves = Stream.of(
                Move.normal(knight, Coordinate.of("b6")),
                Move.normal(knight, Coordinate.of("d6")),
                Move.normal(knight, Coordinate.of("a7")),
                Move.normal(knight, Coordinate.of("e7")),
                Move.normal(bishop, Coordinate.of("b7")),
                Move.normal(bishop, Coordinate.of("d5")),
                Move.normal(bishop, Coordinate.of("e4")),
                Move.capture(bishop, enemyRook),
                Move.normal(rook, Coordinate.of("d6")),
                Move.normal(rook, Coordinate.of("f6")),
                Move.normal(rook, Coordinate.of("g6")),
                Move.normal(rook, Coordinate.of("h6")));

        assertThat(legalityTester.testPseudoLegals(illegalMoves)).isEmpty();
    }

    @Test
    void removeNonEscapingMoves() {
        var position = FenParser.parse("2n1k3/8/8/7r/2b5/8/4Q3/4K3 b - - 0 1");
        var legalityTester = new LegalityTester(position);

        var board = position.board();
        var king = board.king(Color.BLACK);
        var knight = board.pieceAt(Coordinate.of("c8"));
        var bishop = board.pieceAt(Coordinate.of("c4"));
        var rook = board.pieceAt(Coordinate.of("h5"));
        var enemyQueen = board.pieceAt(Coordinate.of("e2"));

        var legalMoves = new Move[] {
            Move.normal(king, Coordinate.of("d8")),
            Move.normal(king, Coordinate.of("d7")),
            Move.normal(king, Coordinate.of("f8")),
            Move.normal(king, Coordinate.of("f7")),
            Move.normal(knight, Coordinate.of("e7")),
            Move.normal(rook, Coordinate.of("e5")),
            Move.normal(bishop, Coordinate.of("e6")),
            Move.capture(bishop, enemyQueen)
        };

        var pseudoLegals = new PseudoLegalGenerator(position).generate();

        assertThat(legalityTester.testPseudoLegals(pseudoLegals))
                .hasSize(legalMoves.length)
                .containsOnlyOnce(legalMoves);
    }
}
