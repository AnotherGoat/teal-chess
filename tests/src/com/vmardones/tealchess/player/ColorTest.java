/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

class ColorTest {

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
    void getPlayer() {
        var whitePlayer = mock(HumanPlayer.class);
        when(whitePlayer.color()).thenReturn(Color.WHITE);
        var blackPlayer = mock(HumanPlayer.class);
        when(blackPlayer.color()).thenReturn(Color.BLACK);

        List<Player> players = List.of(whitePlayer, blackPlayer);

        assertThat(white.player(players)).isEqualTo(whitePlayer);
        assertThat(black.player(players)).isEqualTo(blackPlayer);
    }

    @Test
    void getOpponent() {
        var whitePlayer = mock(HumanPlayer.class);
        when(whitePlayer.color()).thenReturn(Color.WHITE);
        var blackPlayer = mock(HumanPlayer.class);
        when(blackPlayer.color()).thenReturn(Color.BLACK);

        List<Player> players = List.of(whitePlayer, blackPlayer);

        assertThat(white.opponent(players)).isEqualTo(blackPlayer);
        assertThat(black.opponent(players)).isEqualTo(whitePlayer);
    }
}
