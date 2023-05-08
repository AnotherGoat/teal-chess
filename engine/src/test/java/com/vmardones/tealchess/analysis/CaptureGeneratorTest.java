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
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class CaptureGeneratorTest {

    @Test
    void ignoresPawnCaptures() {
        var position = FenParser.parse("4k3/8/8/8/1n1b1r1q/P1P1P1P1/8/4K3 w - - 0 1");
        var generator = new CaptureGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void knightCaptures() {
        var position = FenParser.parse("6k1/1n1BN3/1B6/2n5/P3R3/2Q5/8/4K3 b - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var knight = board.pieceAt(Coordinate.of("c5"));

        var expectedCaptures = new Move[] {
            Move.builder(knight, Coordinate.of("a4")).capture(board.pieceAt(Coordinate.of("a4"))),
            Move.builder(knight, Coordinate.of("e4")).capture(board.pieceAt(Coordinate.of("e4"))),
            Move.builder(knight, Coordinate.of("d7")).capture(board.pieceAt(Coordinate.of("d7")))
        };

        assertThat(generator.generate()).hasSize(3).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void bishopCaptures() {
        var position = FenParser.parse("4k3/b5r1/8/4N3/3B4/4p3/1p6/4K3 w - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt(Coordinate.of("d4"));

        var expectedCaptures = new Move[] {
            Move.builder(bishop, Coordinate.of("a7")).capture(board.pieceAt(Coordinate.of("a7"))),
            Move.builder(bishop, Coordinate.of("b2")).capture(board.pieceAt(Coordinate.of("b2"))),
            Move.builder(bishop, Coordinate.of("e3")).capture(board.pieceAt(Coordinate.of("e3")))
        };

        assertThat(generator.generate()).hasSize(3).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void rookCaptures() {
        var position = FenParser.parse("4k3/p7/8/8/R3Bb2/8/8/n3K3 w - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var rook = board.pieceAt(Coordinate.of("a4"));

        var expectedCaptures = new Move[] {
            Move.builder(rook, Coordinate.of("a7")).capture(board.pieceAt(Coordinate.of("a7"))),
            Move.builder(rook, Coordinate.of("a1")).capture(board.pieceAt(Coordinate.of("a1")))
        };

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void queenCaptures() {
        var position = FenParser.parse("4k3/1p6/2R5/n2Q2N1/3N4/1P1b4/q5b1/4K3 w - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var queen = board.pieceAt(Coordinate.of("d5"));

        var expectedCaptures = new Move[] {
            Move.builder(queen, Coordinate.of("a5")).capture(board.pieceAt(Coordinate.of("a5"))),
            Move.builder(queen, Coordinate.of("g2")).capture(board.pieceAt(Coordinate.of("g2")))
        };

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void kingCaptures() {
        var position = FenParser.parse("3pkb2/3R1N2/8/8/8/8/8/4K3 b - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var queen = board.pieceAt(Coordinate.of("e8"));

        var expectedCaptures = new Move[] {
            Move.builder(queen, Coordinate.of("d7")).capture(board.pieceAt(Coordinate.of("d7"))),
            Move.builder(queen, Coordinate.of("f7")).capture(board.pieceAt(Coordinate.of("f7")))
        };

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }
}
