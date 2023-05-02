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
import com.vmardones.tealchess.parser.FenParser;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class LegalityTesterTest {

    @Test
    void removeSelfChecks() {
        var position = FenParser.parse("R1n1k3/8/2b1r3/8/B7/8/4Q3/4K3 b - - 0 1");
        var legalityTester = new LegalityTester(position);

        var board = position.board();
        var knight = board.pieceAt("c8");
        var bishop = board.pieceAt("c6");
        var rook = board.pieceAt("e6");
        var enemyRook = board.pieceAt("a8");

        var illegalMoves = Stream.of(
                Move.builder(knight, Coordinate.of("b6")).normal(),
                Move.builder(knight, Coordinate.of("d6")).normal(),
                Move.builder(knight, Coordinate.of("a7")).normal(),
                Move.builder(knight, Coordinate.of("e7")).normal(),
                Move.builder(bishop, Coordinate.of("b7")).normal(),
                Move.builder(bishop, Coordinate.of("d5")).normal(),
                Move.builder(bishop, Coordinate.of("e4")).normal(),
                Move.builder(bishop, Coordinate.of("a8")).capture(enemyRook),
                Move.builder(rook, Coordinate.of("d6")).normal(),
                Move.builder(rook, Coordinate.of("f6")).normal(),
                Move.builder(rook, Coordinate.of("g6")).normal(),
                Move.builder(rook, Coordinate.of("h6")).normal());

        assertThat(legalityTester.testPseudoLegals(illegalMoves)).isEmpty();
    }

    @Test
    void removeNonEscapingMoves() {
        var position = FenParser.parse("2n1k3/8/8/7r/2b5/8/4Q3/4K3 b - - 0 1");
        var legalityTester = new LegalityTester(position);

        var board = position.board();
        var king = board.king(Color.BLACK);
        var knight = board.pieceAt("c8");
        var bishop = board.pieceAt("c4");
        var rook = board.pieceAt("h5");
        var enemyQueen = board.pieceAt("e2");

        var legalMoves = new Move[] {
            Move.builder(king, Coordinate.of("d8")).normal(),
            Move.builder(king, Coordinate.of("d7")).normal(),
            Move.builder(king, Coordinate.of("f8")).normal(),
            Move.builder(king, Coordinate.of("f7")).normal(),
            Move.builder(knight, Coordinate.of("e7")).normal(),
            Move.builder(rook, Coordinate.of("e5")).normal(),
            Move.builder(bishop, Coordinate.of("e6")).normal(),
            Move.builder(bishop, Coordinate.of("e2")).capture(enemyQueen)
        };

        var pseudoLegals = new PseudoLegalGenerator(position).generate();

        assertThat(legalityTester.testPseudoLegals(pseudoLegals))
                .hasSize(legalMoves.length)
                .containsOnlyOnce(legalMoves);
    }
}
