/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.color;

import static com.vmardones.tealchess.color.Color.BLACK;
import static com.vmardones.tealchess.color.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

final class ColorTest {

    @Test
    void whiteSymbol() {
        assertThat(Color.fromSymbol("w")).isEqualTo(WHITE);
    }

    @Test
    void blackSymbol() {
        assertThat(Color.fromSymbol("b")).isEqualTo(BLACK);
    }

    @Test
    void unknownSymbol() {
        assertThatThrownBy(() -> Color.fromSymbol("+"))
                .isInstanceOf(ColorSymbolException.class)
                .hasMessageContaining("+");
    }

    @Test
    void isWhite() {
        assertThat(WHITE.isWhite()).isTrue();
        assertThat(BLACK.isWhite()).isFalse();
    }

    @Test
    void isBlack() {
        assertThat(WHITE.isBlack()).isFalse();
        assertThat(BLACK.isBlack()).isTrue();
    }

    @Test
    void whiteFen() {
        assertThat(WHITE.fen()).isEqualTo("w");
    }

    @Test
    void blackFen() {
        assertThat(BLACK.fen()).isEqualTo("b");
    }

    @Test
    void whiteUnicode() {
        assertThat(WHITE.unicode()).isEqualTo("□");
    }

    @Test
    void blackUnicode() {
        assertThat(BLACK.unicode()).isEqualTo("■");
    }
}
