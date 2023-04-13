/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AllianceTest {

    Alliance white = Alliance.WHITE;
    Alliance black = Alliance.BLACK;

    @Test
    void whiteSymbol() {
        assertThat(Alliance.fromSymbol("w")).isEqualTo(white);
    }

    @Test
    void blackSymbol() {
        assertThat(Alliance.fromSymbol("b")).isEqualTo(black);
    }

    @Test
    void unknownSymbol() {
        assertThatThrownBy(() -> Alliance.fromSymbol("+"))
                .isInstanceOf(AllianceSymbolException.class)
                .hasMessageContaining("+");
    }

    @Test
    void whiteOpposite() {
        assertThat(white.opposite()).isEqualTo(black);
    }

    @Test
    void blackOpposite() {
        assertThat(black.opposite()).isEqualTo(white);
    }

    @Test
    void whiteDirection() {
        assertThat(white.direction()).isPositive().isEqualTo(1);
    }

    @Test
    void blackDirection() {
        assertThat(black.direction()).isNegative().isEqualTo(-1);
    }

    @Test
    void whiteOppositeDirection() {
        assertThat(white.oppositeDirection()).isNegative().isEqualTo(-1);
    }

    @Test
    void blackOppositeDirection() {
        assertThat(black.oppositeDirection()).isPositive().isEqualTo(1);
    }
}
