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
import com.vmardones.tealchess.player.PlayerFactory;
import org.junit.jupiter.api.Test;

// TODO: Fix wrong detailed perft implementation
// TODO: Test more perft functions in trivial cases
final class PerftTest {

    Perft perft = new Perft(
            new LegalGenerator(), new MoveMaker(), new PlayerFactory(new AttackGenerator(), new LegalGenerator()));

    @Test
    void simplePerft() {
        assertThat(perft.execute(INITIAL_POSITION, 1)).isEqualTo(20);
    }

    @Test
    void simpleDivide() {
        assertThat(perft.divide(INITIAL_POSITION, 1).values()).hasSize(20).allSatisfy(result -> assertThat(result)
                .isOne());
    }

    @Test
    void divideReport() {
        var report = perft.divideReport(INITIAL_POSITION, 1);

        assertThat(report)
                .containsOnlyOnce("e2e4: 1")
                .containsOnlyOnce("d2d3: 1")
                .containsOnlyOnce("b1a3: 1")
                .containsOnlyOnce("g1f3: 1")
                .endsWith("Nodes searched: 20");
    }
}
