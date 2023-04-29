/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.board.Coordinate;
import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.FenParser;
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
    void knightMoves() {}

    @Test
    void bishopMoves() {}

    @Test
    void rookMoves() {}

    @Test
    void queenMoves() {}

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
