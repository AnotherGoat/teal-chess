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
            Move.builder(king, Coordinate.of("d8")).normal(),
            Move.builder(king, Coordinate.of("f8")).normal(),
            Move.builder(king, Coordinate.of("d7")).normal(),
            Move.builder(king, Coordinate.of("e7")).normal(),
            Move.builder(king, Coordinate.of("f7")).normal()
        };

        assertThat(generator.generate()).hasSize(5).containsOnlyOnce(expectedMoves);
    }

    @Test
    void ignoresPawnPushes() {
        var position = FenParser.parse("4k3/8/pppppppp/8/8/8/8/4K3 b - - 0 1");
        var generator = new NormalGenerator(position);

        var kingMoves = 5;
        assertThat(generator.generate()).hasSize(kingMoves).allMatch(move -> move.piece()
                .isKing());
    }

    @Test
    void knightMoves() {
        var position = FenParser.parse("4k3/1p6/1P1r4/8/2N5/4n3/1q6/4K3 w - - 0 1");
        var generator = new NormalGenerator(position);

        var board = position.board();
        var knight = board.pieceAt(Coordinate.of("c4"));

        var expectedMoves = new Move[] {
            Move.builder(knight, Coordinate.of("a3")).normal(),
            Move.builder(knight, Coordinate.of("a5")).normal(),
            Move.builder(knight, Coordinate.of("d2")).normal(),
            Move.builder(knight, Coordinate.of("e5")).normal()
        };

        var unexpectedMoves = new Move[] {
            Move.builder(knight, Coordinate.of("b6")).normal(),
            Move.builder(knight, Coordinate.of("b2")).normal()
        };

        var kingMoves = 5;
        assertThat(generator.generate())
                .hasSize(kingMoves + 4)
                .containsOnlyOnce(expectedMoves)
                .doesNotContain(unexpectedMoves);
    }

    @Test
    void bishopMoves() {
        var position = FenParser.parse("4k3/6B1/1p6/1P6/3b4/8/8/4K3 b - - 0 1");
        var generator = new NormalGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt(Coordinate.of("d4"));

        var expectedMoves = new Move[] {
            Move.builder(bishop, Coordinate.of("c5")).normal(),
            Move.builder(bishop, Coordinate.of("a1")).normal(),
            Move.builder(bishop, Coordinate.of("b2")).normal(),
            Move.builder(bishop, Coordinate.of("g1")).normal(),
            Move.builder(bishop, Coordinate.of("f6")).normal()
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
            Move.builder(rook, Coordinate.of("d1")).normal(),
            Move.builder(rook, Coordinate.of("b4")).normal(),
            Move.builder(rook, Coordinate.of("d5")).normal(),
            Move.builder(rook, Coordinate.of("d6")).normal(),
            Move.builder(rook, Coordinate.of("f4")).normal()
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
            Move.builder(queen, Coordinate.of("b5")).normal(),
            Move.builder(queen, Coordinate.of("c6")).normal(),
            Move.builder(queen, Coordinate.of("d8")).normal(),
            Move.builder(queen, Coordinate.of("f7")).normal(),
            Move.builder(queen, Coordinate.of("f5")).normal(),
            Move.builder(queen, Coordinate.of("h1")).normal(),
            Move.builder(queen, Coordinate.of("d4")).normal(),
            Move.builder(queen, Coordinate.of("c4")).normal(),
            Move.builder(queen, Coordinate.of("f3")).normal(),
        };

        var kingMoves = 5;
        assertThat(generator.generate()).hasSize(kingMoves + 17).containsOnlyOnce(expectedMoves);
    }

    @Test
    void blockedSlidingMoves() {
        var position = FenParser.parse("4k3/P7/5B2/8/3N4/8/5Q2/R3K3 w - - 0 1");

        var generator = new NormalGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt("f6");
        var rook = board.pieceAt("a1");
        var queen = board.pieceAt("f2");

        var unexpectedMoves = new Move[] {
            Move.builder(bishop, Coordinate.of("d4")).normal(),
            Move.builder(bishop, Coordinate.of("c3")).normal(),
            Move.builder(rook, Coordinate.of("a7")).normal(),
            Move.builder(rook, Coordinate.of("a8")).normal(),
            Move.builder(queen, Coordinate.of("d4")).normal(),
            Move.builder(queen, Coordinate.of("c5")).normal(),
            Move.builder(queen, Coordinate.of("f6")).normal(),
            Move.builder(queen, Coordinate.of("f7")).normal()
        };

        assertThat(generator.generate()).isNotEmpty().doesNotContain(unexpectedMoves);
    }
}
