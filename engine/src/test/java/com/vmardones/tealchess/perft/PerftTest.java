/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.perft;

import static com.vmardones.tealchess.position.Position.INITIAL_POSITION;
import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.generator.AttackGenerator;
import com.vmardones.tealchess.generator.LegalGenerator;
import com.vmardones.tealchess.move.MoveMaker;
import com.vmardones.tealchess.parser.fen.FenParser;
import com.vmardones.tealchess.player.PlayerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class PerftTest {

    Perft perft = new Perft(
            new LegalGenerator(), new MoveMaker(), new PlayerFactory(new AttackGenerator(), new LegalGenerator()));

    // TODO: Add methods that test the perft functions themselves before testing test results
    // TODO: Move the methods that test general perft results to integration tests
    @Test
    void simpleDivide() {
        assertThat(perft.divide(INITIAL_POSITION, 1).values())
                .allSatisfy(result -> assertThat(result).isOne());
    }

    // TODO: Fix wrong detailed perft implementation
    // https://www.chessprogramming.org/Perft_Results#Initial_Position
    @CsvSource({"0, 1", "1, 20", "2, 400", "3, 8902", "4, 197281", "5, 4865609"})
    @ParameterizedTest
    void initialPositionPerft(int depth, long expectedNodes) {
        assertThat(perft.execute(INITIAL_POSITION, depth)).isEqualTo(expectedNodes);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_2
    @CsvSource({"0, 1", "1, 48", "2, 2039", "3, 97862", "4, 4085603"})
    @ParameterizedTest
    void position2Perft(int depth, long expectedNodes) {
        var position2 = FenParser.parse("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertThat(perft.execute(position2, depth)).isEqualTo(expectedNodes);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_3
    @CsvSource({"0, 1", "1, 14", "2, 191", "3, 2812", "4, 43238", "5, 674624"})
    @ParameterizedTest
    void position3Perft(int depth, long expectedNodes) {
        var position3 = FenParser.parse("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
        assertThat(perft.execute(position3, depth)).isEqualTo(expectedNodes);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_4
    @CsvSource({"0, 1", "1, 6", "2, 264", "3, 9467", "4, 422333"})
    @ParameterizedTest
    void position4Perft(int depth, long expectedNodes) {
        var position4 = FenParser.parse("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        assertThat(perft.execute(position4, depth)).isEqualTo(expectedNodes);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_5
    @CsvSource({"0, 1", "1, 44", "2, 1486", "3, 62379", "4, 2103487"})
    @ParameterizedTest
    void position5Perft(int depth, long expectedNodes) {
        var position5 = FenParser.parse("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        assertThat(perft.execute(position5, depth)).isEqualTo(expectedNodes);
    }

    // https://www.chessprogramming.org/Perft_Results#Position_6
    @CsvSource({"0, 1", "1, 46", "2, 2079", "3, 89890", "4, 3894594"})
    @ParameterizedTest
    void position6Perft(int depth, long expectedNodes) {
        var position6 = FenParser.parse("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        assertThat(perft.execute(position6, depth)).isEqualTo(expectedNodes);
    }
}
