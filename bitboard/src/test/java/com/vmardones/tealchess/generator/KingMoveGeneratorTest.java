/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.generator;

import static com.vmardones.tealchess.move.MoveType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.move.Move;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

final class KingMoveGeneratorTest {

    MoveGenerator generator = new KingMoveGenerator();

    @Test
    void normalMoves() {
        var position = FenParser.parse("4k3/8/8/8/8/8/8/4K3 b - - 0 1");

        var expectedMoves = new Move[] {
            new Move(NORMAL, e8, d8),
            new Move(NORMAL, e8, f8),
            new Move(NORMAL, e8, d7),
            new Move(NORMAL, e8, e7),
            new Move(NORMAL, e8, f7),
        };

        assertThat(generator.generate(position)).hasSize(5).containsOnlyOnce(expectedMoves);
    }

    @Test
    void captures() {
        var position = FenParser.parse("3pkb2/3R1N2/8/8/8/8/8/4K3 b - - 0 1");

        var expectedCaptures = new Move[] {new Move(CAPTURE, e8, d7), new Move(CAPTURE, e8, f7)};

        var unexpectedCaptures =
                new Move[] {new Move(CAPTURE, e8, d8), new Move(CAPTURE, e8, f8), new Move(CAPTURE, e8, e7)};

        // Technically this normal move is illegal, but move generators only generate pseudo-legal moves
        var normalMoves = 1;
        assertThat(generator.generate(position))
                .hasSize(normalMoves + 2)
                .containsOnlyOnce(expectedCaptures)
                .doesNotContain(unexpectedCaptures);
    }

    @Test
    void checkedKingCantCastle() {
        var position = FenParser.parse("r3k2r/8/8/8/8/4Q3/8/R3K2R b KQkq - 0 1");

        var normalMoves = 5;
        assertThat(generator.generate(position))
                .hasSize(normalMoves)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);
    }

    @Test
    void noWhiteCastlingRights() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w kq - 0 1");

        var normalMoves = 5;
        assertThat(generator.generate(position))
                .hasSize(normalMoves)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);
    }

    @Test
    void noBlackCastlingRights() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b KQ - 0 1");

        var normalMoves = 5;
        assertThat(generator.generate(position))
                .hasSize(normalMoves)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);
    }

    @Test
    void kingSidePathBlocked() {
        // 1st square blocked
        var position1 = FenParser.parse("r3kn1r/8/8/8/8/8/8/R3K2R b k - 0 1");
        var normalMoves1 = 4;
        assertThat(generator.generate(position1))
                .hasSize(normalMoves1)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);

        // 2nd square blocked
        var position2 = FenParser.parse("r3k1nr/8/8/8/8/8/8/R3K2R b k - 0 1");
        var normalMoves2 = 5;
        assertThat(generator.generate(position2))
                .hasSize(normalMoves2)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);
    }

    @Test
    void queenSidePathBlocked() {
        // 1st square blocked
        var position1 = FenParser.parse("r2nk2r/8/8/8/8/8/8/R3K2R b q - 0 1");
        var normalMoves1 = 4;
        assertThat(generator.generate(position1))
                .hasSize(normalMoves1)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);

        // 2nd square blocked
        var position2 = FenParser.parse("r1n1k2r/8/8/8/8/8/8/R3K2R b q - 0 1");
        var normalMoves2 = 5;
        assertThat(generator.generate(position2))
                .hasSize(normalMoves2)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);

        // 3rd square blocked
        var position3 = FenParser.parse("rn2k2r/8/8/8/8/8/8/R3K2R b q - 0 1");
        var normalMoves3 = 5;
        assertThat(generator.generate(position3))
                .hasSize(normalMoves3)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);
    }

    @Test
    void kingSidePathUnderAttack() {
        // 1st square attacked
        var position1 = FenParser.parse("r3k2r/8/8/1b6/8/8/8/R3K2R w K - 0 1");
        var normalMoves1 = 5;
        assertThat(generator.generate(position1))
                .hasSize(normalMoves1)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);

        // 2nd square attacked
        var position2 = FenParser.parse("r3k2r/8/8/2b5/8/8/8/R3K2R w K - 0 1");
        var normalMoves2 = 5;
        assertThat(generator.generate(position2))
                .hasSize(normalMoves2)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);
    }

    @Test
    void queenSidePathUnderAttack() {
        // 1st square attacked
        var position1 = FenParser.parse("r3k2r/8/8/7b/8/8/8/R3K2R w Q - 0 1");
        var normalMoves1 = 5;
        assertThat(generator.generate(position1))
                .hasSize(normalMoves1)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);

        // 2nd square attacked
        var position2 = FenParser.parse("r3k2r/8/8/6b1/8/8/8/R3K2R w Q - 0 1");
        var normalMoves2 = 5;
        assertThat(generator.generate(position2))
                .hasSize(normalMoves2)
                .noneMatch(move -> move.type() == KING_CASTLE || move.type() == QUEEN_CASTLE);
    }

    @Test
    void normalCastles() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R b kq - 0 1");

        var expectedCastles = new Move[] {Move.BLACK_KING_SIDE_CASTLE.get(0), Move.BLACK_QUEEN_SIDE_CASTLE.get(0)};

        var normalMoves = 5;
        assertThat(generator.generate(position)).hasSize(normalMoves + 2).containsOnlyOnce(expectedCastles);
    }

    @Test
    void verySpecificQueenCastle() {
        // Castling can be done queen side even if the square beside the rook is being attacked
        var position = FenParser.parse("1r2k3/8/8/8/8/8/8/R3K3 w Q - 0 1");

        var expectedCastles = new Move[] {Move.WHITE_QUEEN_SIDE_CASTLE.get(0)};

        var normalMoves = 5;
        assertThat(generator.generate(position)).hasSize(normalMoves + 1).containsOnlyOnce(expectedCastles);
    }
}
