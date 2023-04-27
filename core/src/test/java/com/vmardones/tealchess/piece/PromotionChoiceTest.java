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
    void sanStart() {
        for (var choice : PromotionChoice.values()) {
            assertThat(choice.san()).startsWith("=");
        }
    }

    @Test
    void san() {
        assertThat(PromotionChoice.QUEEN.san()).isEqualTo("=Q");
    }
}
