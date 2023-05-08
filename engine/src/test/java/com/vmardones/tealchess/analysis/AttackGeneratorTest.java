/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Attack;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class AttackGeneratorTest {

    @Test
    void kingAttacks() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var king = board.pieceAt(Coordinate.of("e1"));

        var expectedAttacks = new Attack[] {
            new Attack(king, board.squareAt(Coordinate.of("d1"))),
            new Attack(king, board.squareAt(Coordinate.of("f1"))),
            new Attack(king, board.squareAt(Coordinate.of("d2"))),
            new Attack(king, board.squareAt(Coordinate.of("e2"))),
            new Attack(king, board.squareAt(Coordinate.of("f2")))
        };

        assertThat(generator.calculateAttacks(true)).hasSize(5).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void pawnAttacks() {
        var position = FenParser.parse("4k3/8/8/2p5/8/8/8/4K3 w - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var pawn = board.pieceAt(Coordinate.of("c5"));

        var expectedAttacks = new Attack[] {
            new Attack(pawn, board.squareAt(Coordinate.of("b4"))), new Attack(pawn, board.squareAt(Coordinate.of("d4")))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 2).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void knightAttacks() {
        var position = FenParser.parse("4k3/8/8/8/6N1/8/8/4K3 b - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var knight = board.pieceAt(Coordinate.of("g4"));

        var expectedAttacks = new Attack[] {
            new Attack(knight, board.squareAt(Coordinate.of("e3"))),
            new Attack(knight, board.squareAt(Coordinate.of("f2"))),
            new Attack(knight, board.squareAt(Coordinate.of("h2"))),
            new Attack(knight, board.squareAt(Coordinate.of("f6")))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 6).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void bishopAttacks() {
        var position = FenParser.parse("4k3/8/8/8/3b4/8/8/4K3 w - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt(Coordinate.of("d4"));

        var expectedAttacks = new Attack[] {
            new Attack(bishop, board.squareAt(Coordinate.of("a1"))),
            new Attack(bishop, board.squareAt(Coordinate.of("c3"))),
            new Attack(bishop, board.squareAt(Coordinate.of("a7"))),
            new Attack(bishop, board.squareAt(Coordinate.of("h8"))),
            new Attack(bishop, board.squareAt(Coordinate.of("g1"))),
            new Attack(bishop, board.squareAt(Coordinate.of("f2")))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 13).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void rookAttacks() {
        var position = FenParser.parse("4k3/8/8/8/R7/8/8/4K3 b - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var rook = board.pieceAt(Coordinate.of("a4"));

        var expectedAttacks = new Attack[] {
            new Attack(rook, board.squareAt(Coordinate.of("a1"))),
            new Attack(rook, board.squareAt(Coordinate.of("a3"))),
            new Attack(rook, board.squareAt(Coordinate.of("a7"))),
            new Attack(rook, board.squareAt(Coordinate.of("a8"))),
            new Attack(rook, board.squareAt(Coordinate.of("c4"))),
            new Attack(rook, board.squareAt(Coordinate.of("d4"))),
            new Attack(rook, board.squareAt(Coordinate.of("g4"))),
            new Attack(rook, board.squareAt(Coordinate.of("h4")))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 14).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void queenAttacks() {
        var position = FenParser.parse("4k3/2q5/8/8/8/8/8/4K3 w - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var queen = board.pieceAt(Coordinate.of("c7"));

        var expectedAttacks = new Attack[] {
            new Attack(queen, board.squareAt(Coordinate.of("c8"))),
            new Attack(queen, board.squareAt(Coordinate.of("c1"))),
            new Attack(queen, board.squareAt(Coordinate.of("a7"))),
            new Attack(queen, board.squareAt(Coordinate.of("h7"))),
            new Attack(queen, board.squareAt(Coordinate.of("b8"))),
            new Attack(queen, board.squareAt(Coordinate.of("a5"))),
            new Attack(queen, board.squareAt(Coordinate.of("h2"))),
            new Attack(queen, board.squareAt(Coordinate.of("d8")))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 23).containsOnlyOnce(expectedAttacks);
    }
}
