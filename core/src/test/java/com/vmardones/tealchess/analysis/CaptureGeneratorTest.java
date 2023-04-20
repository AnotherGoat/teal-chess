/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.FenParser;
import org.junit.jupiter.api.Test;

class CaptureGeneratorTest {

    @Test
    void pawnCaptures() {
        var position = FenParser.parse("4k3/8/8/1BpR4/1NPN4/8/8/4K3 b - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var pawn = board.pieceAt("c5");

        var expectedCaptures = new Move[] {
            Move.createCapture(pawn, Coordinate.of("b4"), board.pieceAt("b4")),
            Move.createCapture(pawn, Coordinate.of("d4"), board.pieceAt("d4"))
        };

        assertThat(generator.calculateCaptures()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void knightCaptures() {
        var position = FenParser.parse("6k1/1n1BN3/1B6/2n5/P3R3/2Q5/8/4K3 b - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var knight = board.pieceAt("c5");

        var expectedCaptures = new Move[] {
            Move.createCapture(knight, Coordinate.of("a4"), board.pieceAt("a4")),
            Move.createCapture(knight, Coordinate.of("e4"), board.pieceAt("e4")),
            Move.createCapture(knight, Coordinate.of("d7"), board.pieceAt("d7"))
        };

        assertThat(generator.calculateCaptures()).hasSize(3).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void bishopCaptures() {
        var position = FenParser.parse("4k3/b5r1/8/4N3/3B4/4p3/1p6/4K3 w - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt("d4");

        var expectedCaptures = new Move[] {
            Move.createCapture(bishop, Coordinate.of("a7"), board.pieceAt("a7")),
            Move.createCapture(bishop, Coordinate.of("b2"), board.pieceAt("b2")),
            Move.createCapture(bishop, Coordinate.of("e3"), board.pieceAt("e3"))
        };

        assertThat(generator.calculateCaptures()).hasSize(3).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void rookCaptures() {
        var position = FenParser.parse("4k3/p7/8/8/R3Bb2/8/8/n3K3 w - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var rook = board.pieceAt("a4");

        var expectedCaptures = new Move[] {
            Move.createCapture(rook, Coordinate.of("a7"), board.pieceAt("a7")),
            Move.createCapture(rook, Coordinate.of("a1"), board.pieceAt("a1"))
        };

        assertThat(generator.calculateCaptures()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void queenCaptures() {
        var position = FenParser.parse("4k3/1p6/2R5/n2Q2N1/3N4/1P1b4/q5b1/4K3 w - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var queen = board.pieceAt("d5");

        var expectedCaptures = new Move[] {
            Move.createCapture(queen, Coordinate.of("a5"), board.pieceAt("a5")),
            Move.createCapture(queen, Coordinate.of("g2"), board.pieceAt("g2"))
        };

        assertThat(generator.calculateCaptures()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }

    @Test
    void kingCaptures() {
        var position = FenParser.parse("3pkb2/3R1N2/8/8/8/8/8/4K3 b - - 0 1");
        var generator = new CaptureGenerator(position);

        var board = position.board();
        var queen = board.pieceAt("e8");

        var expectedCaptures = new Move[] {
            Move.createCapture(queen, Coordinate.of("d7"), board.pieceAt("d7")),
            Move.createCapture(queen, Coordinate.of("f7"), board.pieceAt("f7"))
        };

        assertThat(generator.calculateCaptures()).hasSize(2).containsOnlyOnce(expectedCaptures);
    }
}
