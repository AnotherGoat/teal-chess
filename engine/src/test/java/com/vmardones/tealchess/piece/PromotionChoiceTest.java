/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static com.vmardones.tealchess.piece.PromotionChoice.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

final class PromotionChoiceTest {

    @Test
    void san() {
        assertThat(QUEEN.san()).isEqualTo("Q");
        assertThat(KNIGHT.san()).isEqualTo("N");
        assertThat(ROOK.san()).isEqualTo("R");
        assertThat(BISHOP.san()).isEqualTo("B");
    }
}
