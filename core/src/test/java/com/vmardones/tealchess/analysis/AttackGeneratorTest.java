/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Attack;
import com.vmardones.tealchess.parser.FenParser;
import org.junit.jupiter.api.Test;

class AttackGeneratorTest {

    @Test
    void kingAttacks() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var king = board.pieceAt("e1");

        var expectedAttacks = new Attack[] {
            new Attack(king, board.squareAt("d1")),
            new Attack(king, board.squareAt("f1")),
            new Attack(king, board.squareAt("d2")),
            new Attack(king, board.squareAt("e2")),
            new Attack(king, board.squareAt("f2"))
        };

        assertThat(generator.calculateAttacks(true)).hasSize(5).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void pawnAttacks() {
        var position = FenParser.parse("4k3/8/8/2p5/8/8/8/4K3 w - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var pawn = board.pieceAt("c5");

        var expectedAttacks =
                new Attack[] {new Attack(pawn, board.squareAt("b4")), new Attack(pawn, board.squareAt("d4"))};
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 2).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void knightAttacks() {
        var position = FenParser.parse("4k3/8/8/8/6N1/8/8/4K3 b - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var knight = board.pieceAt("g4");

        var expectedAttacks = new Attack[] {
            new Attack(knight, board.squareAt("e3")),
            new Attack(knight, board.squareAt("f2")),
            new Attack(knight, board.squareAt("h2")),
            new Attack(knight, board.squareAt("f6"))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 6).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void bishopAttacks() {
        var position = FenParser.parse("4k3/8/8/8/3b4/8/8/4K3 w - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var bishop = board.pieceAt("d4");

        var expectedAttacks = new Attack[] {
            new Attack(bishop, board.squareAt("a1")),
            new Attack(bishop, board.squareAt("c3")),
            new Attack(bishop, board.squareAt("a7")),
            new Attack(bishop, board.squareAt("h8")),
            new Attack(bishop, board.squareAt("g1")),
            new Attack(bishop, board.squareAt("f2"))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 13).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void rookAttacks() {
        var position = FenParser.parse("4k3/8/8/8/R7/8/8/4K3 b - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var rook = board.pieceAt("a4");

        var expectedAttacks = new Attack[] {
            new Attack(rook, board.squareAt("a1")),
            new Attack(rook, board.squareAt("a3")),
            new Attack(rook, board.squareAt("a7")),
            new Attack(rook, board.squareAt("a8")),
            new Attack(rook, board.squareAt("c4")),
            new Attack(rook, board.squareAt("d4")),
            new Attack(rook, board.squareAt("g4")),
            new Attack(rook, board.squareAt("h4"))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 14).containsOnlyOnce(expectedAttacks);
    }

    @Test
    void queenAttacks() {
        var position = FenParser.parse("4k3/2q5/8/8/8/8/8/4K3 w - - 0 1");
        var generator = new AttackGenerator(position);

        var board = position.board();
        var queen = board.pieceAt("c7");

        var expectedAttacks = new Attack[] {
            new Attack(queen, board.squareAt("c8")),
            new Attack(queen, board.squareAt("c1")),
            new Attack(queen, board.squareAt("a7")),
            new Attack(queen, board.squareAt("h7")),
            new Attack(queen, board.squareAt("b8")),
            new Attack(queen, board.squareAt("a5")),
            new Attack(queen, board.squareAt("h2")),
            new Attack(queen, board.squareAt("d8"))
        };
        var kingAttacks = 5;

        assertThat(generator.calculateAttacks(true)).hasSize(kingAttacks + 23).containsOnlyOnce(expectedAttacks);
    }
}
