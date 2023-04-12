/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cl.vmardones.chess.engine.board.BoardDirector;
import cl.vmardones.chess.engine.board.Position;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

    Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void firstTurn() {
        assertThat(game.board()).isEqualTo(BoardDirector.createStandardBoard());
        assertThat(game.currentPlayer().alliance()).isEqualTo(Alliance.WHITE);
        assertThat(game.currentOpponent().alliance()).isEqualTo(Alliance.BLACK);
        assertThat(game.history().lastMove()).isNull();
    }

    @Test
    void secondTurn() {
        var initialBoard = game.board();
        var move = mock(Move.class);

        var piece = new Pawn("d5", Alliance.WHITE);
        when(move.piece()).thenReturn(piece);
        when(move.destination()).thenReturn(Position.of("d4"));

        game.addTurn(move);

        assertThat(game.board()).isNotEqualTo(initialBoard);
        assertThat(game.currentPlayer().alliance()).isEqualTo(Alliance.BLACK);
        assertThat(game.currentOpponent().alliance()).isEqualTo(Alliance.WHITE);
        assertThat(game.history().lastMove()).isEqualTo(move);
    }
}
