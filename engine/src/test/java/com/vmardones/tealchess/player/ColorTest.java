/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;


import com.vmardones.tealchess.ExcludeFromNullAway;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class ColorTest {

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

    @Test
    void asString() {
        assertThat(white).hasToString("White");
        assertThat(black).hasToString("Black");
    }
}
