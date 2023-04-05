/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AllianceTest {

    Alliance white = Alliance.WHITE;
    Alliance black = Alliance.BLACK;

    @Mock
    HumanPlayer whitePlayer;

    @Mock
    HumanPlayer blackPlayer;

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
    void chooseWhitePlayer() {
        when(whitePlayer.alliance()).thenReturn(white);
        when(blackPlayer.alliance()).thenReturn(black);

        List<Player> players = List.of(blackPlayer, whitePlayer);

        assertThat(white.choosePlayer(players)).isEqualTo(whitePlayer);
    }

    @Test
    void chooseBlackPlayer() {
        when(whitePlayer.alliance()).thenReturn(white);
        when(blackPlayer.alliance()).thenReturn(black);

        List<Player> players = List.of(whitePlayer, blackPlayer);

        assertThat(black.choosePlayer(players)).isEqualTo(blackPlayer);
    }
}
