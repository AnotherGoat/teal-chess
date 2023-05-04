/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vmardones.tealchess.ExcludeFromNullAway;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class ColorTest {

    Color white = Color.WHITE;
    Color black = Color.BLACK;

    @Test
    void whiteSymbol() {
        assertThat(Color.fromSymbol("w")).isEqualTo(white);
    }

    @Test
    void blackSymbol() {
        assertThat(Color.fromSymbol("b")).isEqualTo(black);
    }

    @Test
    void unknownSymbol() {
        assertThatThrownBy(() -> Color.fromSymbol("+"))
                .isInstanceOf(ColorSymbolException.class)
                .hasMessageContaining("+");
    }

    @Test
    void isWhite() {
        assertThat(white.isWhite()).isTrue();
        assertThat(black.isWhite()).isFalse();
    }

    @Test
    void isBlack() {
        assertThat(white.isBlack()).isFalse();
        assertThat(black.isBlack()).isTrue();
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

    @Test
    void whiteFen() {
        assertThat(white.fen()).isEqualTo("w");
    }

    @Test
    void blackFen() {
        assertThat(black.fen()).isEqualTo("b");
    }

    @Test
    void asString() {
        assertThat(white).hasToString("White");
        assertThat(black).hasToString("Black");
    }
}
