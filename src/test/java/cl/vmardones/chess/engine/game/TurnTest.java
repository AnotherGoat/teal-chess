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
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.HumanPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TurnTest {

    @Mock
    Board board;

    @Mock
    HumanPlayer whitePlayer;

    @Mock
    HumanPlayer blackPlayer;

    @Test
    void getWhitePlayer() {
        var whiteTurn = new Turn(board, Alliance.WHITE, whitePlayer, blackPlayer, null);
        assertThat(whiteTurn.player()).isEqualTo(whitePlayer);
    }

    @Test
    void getBlackPlayer() {
        var blackTurn = new Turn(board, Alliance.BLACK, whitePlayer, blackPlayer, null);
        assertThat(blackTurn.player()).isEqualTo(blackPlayer);
    }

    @Test
    void getWhiteOpponent() {
        var whiteTurn = new Turn(board, Alliance.WHITE, whitePlayer, blackPlayer, null);
        assertThat(whiteTurn.opponent()).isEqualTo(blackPlayer);
    }

    @Test
    void getBlackOpponent() {
        var blackTurn = new Turn(board, Alliance.BLACK, whitePlayer, blackPlayer, null);
        assertThat(blackTurn.opponent()).isEqualTo(whitePlayer);
    }

    @Test
    void unmodifiablePlayerLegals() {
        var turn = new Turn(board, Alliance.WHITE, whitePlayer, blackPlayer, null);

        when(whitePlayer.legals()).thenReturn(new ArrayList<>());
        var playerLegals = turn.playerLegals();

        assertThatThrownBy(playerLegals::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableOpponentLegals() {
        var turn = new Turn(board, Alliance.WHITE, whitePlayer, blackPlayer, null);

        when(blackPlayer.legals()).thenReturn(new ArrayList<>());
        var opponentLegals = turn.opponentLegals();

        assertThatThrownBy(opponentLegals::clear).isInstanceOf(UnsupportedOperationException.class);
    }
}
