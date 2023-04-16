/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.player.Color;
import cl.vmardones.chess.engine.player.HumanPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PositionTest {

    @Mock
    Board board;

    @Mock
    HumanPlayer whitePlayer;

    @Mock
    HumanPlayer blackPlayer;

    @Test
    void getWhitePlayer() {
        var whitePosition = new Position(board, Color.WHITE, whitePlayer, blackPlayer, null);
        assertThat(whitePosition.player()).isEqualTo(whitePlayer);
    }

    @Test
    void getBlackPlayer() {
        var blackPosition = new Position(board, Color.BLACK, whitePlayer, blackPlayer, null);
        assertThat(blackPosition.player()).isEqualTo(blackPlayer);
    }

    @Test
    void getWhiteOpponent() {
        var whitePosition = new Position(board, Color.WHITE, whitePlayer, blackPlayer, null);
        assertThat(whitePosition.opponent()).isEqualTo(blackPlayer);
    }

    @Test
    void getBlackOpponent() {
        var blackPosition = new Position(board, Color.BLACK, whitePlayer, blackPlayer, null);
        assertThat(blackPosition.opponent()).isEqualTo(whitePlayer);
    }

    @Test
    void unmodifiablePlayerLegals() {
        var position = new Position(board, Color.WHITE, whitePlayer, blackPlayer, null);

        when(whitePlayer.legals()).thenReturn(new ArrayList<>());
        var playerLegals = position.playerLegals();

        assertThatThrownBy(playerLegals::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableOpponentLegals() {
        var position = new Position(board, Color.WHITE, whitePlayer, blackPlayer, null);

        when(blackPlayer.legals()).thenReturn(new ArrayList<>());
        var opponentLegals = position.opponentLegals();

        assertThatThrownBy(opponentLegals::clear).isInstanceOf(UnsupportedOperationException.class);
    }
}
