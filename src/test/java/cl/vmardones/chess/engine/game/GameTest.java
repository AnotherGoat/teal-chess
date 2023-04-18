/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cl.vmardones.chess.engine.board.Coordinate;
import cl.vmardones.chess.engine.move.Move;
import cl.vmardones.chess.engine.piece.Pawn;
import cl.vmardones.chess.engine.player.Color;
import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    void initialPosition() {
        var game = new Game();

        assertThat(game.board()).isEqualTo(Position.INITIAL_POSITION.board());
        assertThat(game.currentPlayer().color()).isEqualTo(Color.WHITE);
        assertThat(game.currentOpponent().color()).isEqualTo(Color.BLACK);
        assertThat(game.history().lastMove()).isNull();
    }

    @Test
    void secondPosition() {
        var game = new Game();

        var initialBoard = game.board();
        var move = mock(Move.class);

        var piece = new Pawn("d5", Color.WHITE);
        when(move.piece()).thenReturn(piece);
        when(move.destination()).thenReturn(Coordinate.of("d4"));

        game.updatePosition(move);

        assertThat(game.board()).isNotEqualTo(initialBoard);
        assertThat(game.currentPlayer().color()).isEqualTo(Color.BLACK);
        assertThat(game.currentOpponent().color()).isEqualTo(Color.WHITE);
        assertThat(game.history().lastMove()).isEqualTo(move);
    }
}
