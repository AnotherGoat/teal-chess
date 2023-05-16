/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.player.Color;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class NormalGeneratorTest {

    @Test
    void kingMoves() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b - - 0 1");
        var generator = new NormalGenerator(position);

        var board = position.board();
        var king = board.king(Color.BLACK);

        var expectedMoves = new Move[] {
            Move.normal(king, Coordinate.of("d8")),
            Move.normal(king, Coordinate.of("f8")),
            Move.normal(king, Coordinate.of("d7")),
            Move.normal(king, Coordinate.of("e7")),
            Move.normal(king, Coordinate.of("f7"))
        };

        assertThat(generator.generate()).hasSize(5).containsOnlyOnce(expectedMoves);
    }

    @Test
    void bishopMoves() {
        var position = FenParser.parse("4k3/6B1/1p6/1P6/3b4/8/8/4K3 b - - 0 1");
        var generator = new NormalGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt(Coordinate.of("d4"));

        var expectedMoves = new Move[] {
            Move.normal(bishop, Coordinate.of("c5")),
            Move.normal(bishop, Coordinate.of("a1")),
            Move.normal(bishop, Coordinate.of("b2")),
            Move.normal(bishop, Coordinate.of("g1")),
            Move.normal(bishop, Coordinate.of("f6"))
        };

        var kingMoves = 5;
        assertThat(generator.generate()).hasSize(kingMoves + 9).containsOnlyOnce(expectedMoves);
    }

    @Test
    void rookMoves() {
        var position = FenParser.parse("4k3/3r4/8/6p1/3R2P1/8/8/4K3 w - - 0 1");
        var generator = new NormalGenerator(position);

        var board = position.board();
        var rook = board.pieceAt(Coordinate.of("d4"));

        var expectedMoves = new Move[] {
            Move.normal(rook, Coordinate.of("d1")),
            Move.normal(rook, Coordinate.of("b4")),
            Move.normal(rook, Coordinate.of("d5")),
            Move.normal(rook, Coordinate.of("d6")),
            Move.normal(rook, Coordinate.of("f4"))
        };

        var kingMoves = 5;
        assertThat(generator.generate()).hasSize(kingMoves + 10).containsOnlyOnce(expectedMoves);
    }

    @Test
    void queenMoves() {
        var position = FenParser.parse("4k3/1N6/8/R2q2p1/6P1/1p1B4/1P6/4K3 b - - 0 1");
        var generator = new NormalGenerator(position);

        var board = position.board();
        var queen = board.pieceAt(Coordinate.of("d5"));

        var expectedMoves = new Move[] {
            Move.normal(queen, Coordinate.of("b5")),
            Move.normal(queen, Coordinate.of("c6")),
            Move.normal(queen, Coordinate.of("d8")),
            Move.normal(queen, Coordinate.of("f7")),
            Move.normal(queen, Coordinate.of("f5")),
            Move.normal(queen, Coordinate.of("h1")),
            Move.normal(queen, Coordinate.of("d4")),
            Move.normal(queen, Coordinate.of("c4")),
            Move.normal(queen, Coordinate.of("f3")),
        };

        var kingMoves = 5;
        assertThat(generator.generate()).hasSize(kingMoves + 17).containsOnlyOnce(expectedMoves);
    }

    @Test
    void blockedSlidingMoves() {
        var position = FenParser.parse("4k3/P7/5B2/8/3N4/8/5Q2/R3K3 w - - 0 1");

        var generator = new NormalGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt(Coordinate.of("f6"));
        var rook = board.pieceAt(Coordinate.of("a1"));
        var queen = board.pieceAt(Coordinate.of("f2"));

        var unexpectedMoves = new Move[] {
            Move.normal(bishop, Coordinate.of("d4")),
            Move.normal(bishop, Coordinate.of("c3")),
            Move.normal(rook, Coordinate.of("a7")),
            Move.normal(rook, Coordinate.of("a8")),
            Move.normal(queen, Coordinate.of("d4")),
            Move.normal(queen, Coordinate.of("c5")),
            Move.normal(queen, Coordinate.of("f6")),
            Move.normal(queen, Coordinate.of("f7"))
        };

        assertThat(generator.generate()).isNotEmpty().doesNotContain(unexpectedMoves);
    }
}
