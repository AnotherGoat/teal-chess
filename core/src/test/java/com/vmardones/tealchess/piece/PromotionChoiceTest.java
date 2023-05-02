/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.piece;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class PromotionChoiceTest {

    @Test
    void san() {
        assertThat(PromotionChoice.QUEEN.san()).isEqualTo("Q");
        assertThat(PromotionChoice.KNIGHT.san()).isEqualTo("N");
        assertThat(PromotionChoice.ROOK.san()).isEqualTo("R");
        assertThat(PromotionChoice.BISHOP.san()).isEqualTo("B");
    }
}
