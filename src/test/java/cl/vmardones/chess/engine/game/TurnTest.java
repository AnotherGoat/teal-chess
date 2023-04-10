/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;

import cl.vmardones.chess.engine.board.Board;
import cl.vmardones.chess.engine.player.Alliance;
import cl.vmardones.chess.engine.player.HumanPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TurnTest {

    Turn whiteTurn;
    Turn blackTurn;

    @Mock
    Board board;

    @Mock
    HumanPlayer whitePlayer;

    @Mock
    HumanPlayer blackPlayer;

    @BeforeEach
    void setUp() {
        whiteTurn = new Turn(board, Alliance.WHITE, whitePlayer, blackPlayer, null);
        blackTurn = new Turn(board, Alliance.BLACK, whitePlayer, blackPlayer, null);
    }

    @Test
    void getWhitePlayer() {
        assertThat(whiteTurn.player()).isEqualTo(whitePlayer);
    }

    @Test
    void getBlackPlayer() {
        assertThat(blackTurn.player()).isEqualTo(blackPlayer);
    }

    @Test
    void getWhiteOpponent() {
        assertThat(whiteTurn.opponent()).isEqualTo(blackPlayer);
    }

    @Test
    void getBlackOpponent() {
        assertThat(blackTurn.opponent()).isEqualTo(whitePlayer);
    }
}
