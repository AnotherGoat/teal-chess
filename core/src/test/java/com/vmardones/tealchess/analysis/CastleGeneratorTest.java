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
import com.vmardones.tealchess.piece.King;
import com.vmardones.tealchess.piece.Rook;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class CastleGeneratorTest {

    @Test
    void kingInCheck() {
        var position = FenParser.parse("r3k2r/8/8/8/8/4Q3/8/R3K2R b KQkq - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void noWhiteCastlingRights() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w kq - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void noBlackCastlingRights() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b KQ - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void kingSideRookIsMissing() {
        var position = FenParser.parse("r3k3/8/8/8/8/8/8/R3K2R b k - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void queenSideRookIsMissing() {
        var position = FenParser.parse("4k2r/8/8/8/8/8/8/R3K2R b q - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();
    }

    @Test
    void kingSidePathBlocked() {
        // 1st square blocked
        var position = FenParser.parse("r3kn1r/8/8/8/8/8/8/R3K2R b k - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();

        // 2nd square blocked
        var otherPosition = FenParser.parse("r3k1nr/8/8/8/8/8/8/R3K2R b k - 0 1");
        var otherGenerator = new CastleGenerator(otherPosition);

        assertThat(otherGenerator.generate()).isEmpty();
    }

    @Test
    void queenSidePathBlocked() {
        // 1st square blocked
        var position = FenParser.parse("r2nk2r/8/8/8/8/8/8/R3K2R b q - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();

        // 2nd square blocked
        var otherPosition = FenParser.parse("r1n1k2r/8/8/8/8/8/8/R3K2R b q - 0 1");
        var otherGenerator = new CastleGenerator(otherPosition);

        assertThat(otherGenerator.generate()).isEmpty();

        // 3rd square blocked
        var extraPosition = FenParser.parse("rn2k2r/8/8/8/8/8/8/R3K2R b q - 0 1");
        var extraGenerator = new CastleGenerator(extraPosition);

        assertThat(extraGenerator.generate()).isEmpty();
    }

    @Test
    void kingSidePathUnderAttack() {
        // 1st square attacked
        var position = FenParser.parse("r3k2r/8/8/1b6/8/8/8/R3K2R w K - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();

        // 2nd square attacked
        var otherPosition = FenParser.parse("r3k2r/8/8/2b5/8/8/8/R3K2R w K - 0 1");
        var otherGenerator = new CastleGenerator(otherPosition);

        assertThat(otherGenerator.generate()).isEmpty();
    }

    @Test
    void queenSidePathUnderAttack() {
        // 1st square attacked
        var position = FenParser.parse("r3k2r/8/8/7b/8/8/8/R3K2R w Q - 0 1");
        var generator = new CastleGenerator(position);

        assertThat(generator.generate()).isEmpty();

        // 2nd square attacked
        var otherPosition = FenParser.parse("r3k2r/8/8/6b1/8/8/8/R3K2R w Q - 0 1");
        var otherGenerator = new CastleGenerator(otherPosition);

        assertThat(otherGenerator.generate()).isEmpty();
    }

    @Test
    void normalCastles() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b kq - 0 1");
        var generator = new CastleGenerator(position);

        var board = position.board();
        var king = (King) board.pieceAt("e8");
        var kingSideRook = (Rook) board.pieceAt("h8");
        var queenSideRook = (Rook) board.pieceAt("a8");

        var expectedCastles = new Move[] {
            Move.builder(king, Coordinate.of("g8")).castle(true, kingSideRook, Coordinate.of("f8")),
            Move.builder(king, Coordinate.of("c8")).castle(false, queenSideRook, Coordinate.of("d8")),
        };

        assertThat(generator.generate()).hasSize(2).containsOnlyOnce(expectedCastles);
    }

    @Test
    void verySpecificQueenCastle() {
        // Castling can be done queen side even if the square beside the rook is being attacked
        var position = FenParser.parse("1r5k/8/8/8/8/8/8/R3K3 w Q - 0 1");
        var generator = new CastleGenerator(position);

        var board = position.board();
        var king = (King) board.pieceAt("e1");
        var rook = (Rook) board.pieceAt("a1");

        var expectedCastles =
                new Move[] {Move.builder(king, Coordinate.of("c1")).castle(false, rook, Coordinate.of("d1"))};

        assertThat(generator.generate()).hasSize(1).containsOnlyOnce(expectedCastles);
    }
}
