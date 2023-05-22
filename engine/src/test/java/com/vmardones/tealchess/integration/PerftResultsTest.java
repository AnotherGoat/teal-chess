/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.integration;

import static com.vmardones.tealchess.position.Position.INITIAL_POSITION;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.generator.AttackGenerator;
import com.vmardones.tealchess.generator.LegalGenerator;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.perft.Perft;
import com.vmardones.tealchess.player.PlayerFactory;
import org.junit.jupiter.api.Test;

final class PerftResultsTest {

    Perft perft = new Perft(
            new LegalGenerator(), new MoveMaker(), new PlayerFactory(new AttackGenerator(), new LegalGenerator()));

    // https://www.chessprogramming.org/Perft_Results#Initial_Position
    @Test
    void initialPosition() {
        assertThat(perft.execute(INITIAL_POSITION, 5)).isEqualTo(4_865_609L);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_2
    @Test
    void kiwipete() {
        var position = FenParser.parse("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertThat(perft.execute(position, 4)).isEqualTo(4_085_603L);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_3
    @Test
    void position3() {
        var position = FenParser.parse("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
        assertThat(perft.execute(position, 5)).isEqualTo(674_624L);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_4
    @Test
    void position4() {
        var position = FenParser.parse("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        assertThat(perft.execute(position, 4)).isEqualTo(422_333L);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_5
    @Test
    void position5() {
        var position = FenParser.parse("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        assertThat(perft.execute(position, 4)).isEqualTo(2_103_487L);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_6
    @Test
    void position6() {
        var position = FenParser.parse("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        assertThat(perft.execute(position, 4)).isEqualTo(3_894_594L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void avoidIllegalEnPassantCapture1() {
        var depth = 6;
        var expectedNodes = 1_134_888L;

        var position1 = FenParser.parse("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/8/8/8/k1p4R/8/3P4/3K4 w - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void avoidIllegalEnPassantCapture2() {
        var depth = 6;
        var expectedNodes = 1_015_133L;

        var position1 = FenParser.parse("8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/b2p2k1/8/2P5/8/4K3/8/8 b - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void enPassantCaptureChecksOpponent() {
        var depth = 6;
        var expectedNodes = 1_440_467L;

        var position1 = FenParser.parse("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void shortCastlingGivesCheck() {
        var depth = 6;
        var expectedNodes = 661_072L;

        var position1 = FenParser.parse("5k2/8/8/8/8/8/8/4K2R w K - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("4k2r/8/8/8/8/8/8/5K2 b k - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void longCastlingGivesCheck() {
        var depth = 6;
        var expectedNodes = 803_711L;

        var position1 = FenParser.parse("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("r3k3/8/8/8/8/8/8/3K4 b q - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void castling() {
        var depth = 4;
        var expectedNodes = 1_274_206L;

        var position1 = FenParser.parse("r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("r3k2r/7b/8/8/8/8/1B4BQ/R3K2R b KQkq - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void castlingPrevented() {
        var depth = 4;
        var expectedNodes = 1_720_476L;

        var position1 = FenParser.parse("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("r3k2r/8/5Q2/8/8/3q4/8/R3K2R w KQkq - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void promoteOutOfCheck() {
        var depth = 6;
        var expectedNodes = 3_821_001L;

        var position1 = FenParser.parse("2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("3K4/8/8/8/8/8/4p3/2k2R2 b - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void discoveredCheck() {
        var depth = 5;
        var expectedNodes = 1_004_658L;

        var position1 = FenParser.parse("8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("5K2/8/1Q6/2N5/8/1p2k3/8/8 w - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void promoteToGiveCheck() {
        var depth = 6;
        var expectedNodes = 217_342L;

        var position1 = FenParser.parse("4k3/1P6/8/8/8/8/K7/8 w - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/k7/8/8/8/8/1p6/4K3 b - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void underPromoteToCheck() {
        var depth = 6;
        var expectedNodes = 92_683L;

        var position1 = FenParser.parse("8/P1k5/K7/8/8/8/8/8 w - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/8/8/8/8/k7/p1K5/8 b - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void selfStalemate() {
        var depth = 6;
        var expectedNodes = 2_217L;

        var position1 = FenParser.parse("K1k5/8/P7/8/8/8/8/8 w - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/8/8/8/8/p7/8/k1K5 b - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void stalemateAndCheckmate() {
        var depth = 7;
        var expectedNodes = 567_584L;

        var position1 = FenParser.parse("8/k1P5/8/1K6/8/8/8/8 w - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/8/8/8/1k6/8/K1p5/8 b - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&start=10#p508921
    @Test
    void doubleCheck() {
        var depth = 4;
        var expectedNodes = 23_527L;

        var position1 = FenParser.parse("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1");
        assertThat(perft.execute(position1, depth)).isEqualTo(expectedNodes);

        var position2 = FenParser.parse("8/5k2/8/5N2/5Q2/2K5/8/8 w - - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // http://www.rocechess.ch/perft.html
    @Test
    void goodTestPosition() {
        var position = FenParser.parse("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertThat(perft.execute(position, 4)).isEqualTo(4_085_603L);
    }

    // http://www.rocechess.ch/perft.html
    @Test
    void discoverPromotionBugs() {
        var position = FenParser.parse("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - - 0 1");
        assertThat(perft.execute(position, 5)).isEqualTo(3_605_103L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?t=49000#p530237
    @Test
    void wideOpen() {
        var position = FenParser.parse("rnbqkbnr/8/8/8/8/8/8/RNBQKBNR w KQkq - 0 1");
        assertThat(perft.execute(position, 4)).isEqualTo(4_200_525L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?t=49000#p530240
    @Test
    void kingAndPawns() {
        var position = FenParser.parse("4k3/pppppppp/8/8/8/8/PPPPPPPP/4K3 w - - 0 1");
        assertThat(perft.execute(position, 5)).isEqualTo(1_683_597L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?t=49000#p530320
    @Test
    void kingAndRooks() {
        var position = FenParser.parse("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        assertThat(perft.execute(position, 5)).isEqualTo(7_594_526L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?t=49000#p530323
    @Test
    void kingAndBishops() {
        var position = FenParser.parse("2b1kb2/8/8/8/8/8/8/2B1KB2 w - - 0 1");
        assertThat(perft.execute(position, 5)).isEqualTo(1_879_563L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?t=49000#p530326
    @Test
    void kingAndKnights() {
        var position = FenParser.parse("1n2k1n1/8/8/8/8/8/8/1N2K1N1 w - - 0 1");
        assertThat(perft.execute(position, 6)).isEqualTo(3_736_106L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?t=49000#p530335
    @Test
    void kingAndQueen() {
        var position = FenParser.parse("3qk3/8/8/8/8/8/8/3QK3 w - - 0 1");
        assertThat(perft.execute(position, 5)).isEqualTo(2_409_458L);
    }

    // https://www.talkchess.com/forum3/viewtopic.php?p=796668#p796647
    @Test
    void contrivedPosition() {
        var position = FenParser.parse("r3k2r/1bp2pP1/5n2/1P1Q4/1pPq4/5N2/1B1P2p1/R3K2R b KQkq c3 0 1");
        assertThat(perft.execute(position, 4)).isEqualTo(4_812_099L);
    }
}
