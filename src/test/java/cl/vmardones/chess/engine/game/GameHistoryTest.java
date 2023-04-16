/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import cl.vmardones.chess.engine.move.Move;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GameHistoryTest {

    @Test
    void startWithNullMove() {
        var history = new GameHistory();

        assertThat(history.lastMove()).isNull();
        assertThat(history.moves()).isEmpty();
    }

    @Test
    void addToHistory() {
        var history = new GameHistory();

        var turn = mock(Position.class);
        var move = mock(Move.class);
        when(turn.lastMove()).thenReturn(move);

        var newHistory = history.add(new PositionMemento(turn));

        assertThat(newHistory).isNotSameAs(history);
        assertThat(newHistory.moves()).isNotSameAs(history.moves()).hasSize(1);
    }

    @Test
    void moveHistory() {
        var history = new GameHistory();

        var turn1 = mock(Position.class);
        var move1 = mock(Move.class);
        when(turn1.lastMove()).thenReturn(move1);

        var turn2 = mock(Position.class);
        var move2 = mock(Move.class);
        when(turn2.lastMove()).thenReturn(move2);

        var finalHistory = history.add(new PositionMemento(turn1)).add(new PositionMemento(turn2));

        var expectedMoves = List.of(move1, move2);
        var actualMoves = finalHistory.moves();

        assertThat(actualMoves).isEqualTo(expectedMoves);
    }

    @Test
    void unmodifiableMoves() {
        var moves = new GameHistory().moves();

        assertThatThrownBy(moves::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void lastMove() {
        var history = new GameHistory();

        var turn1 = mock(Position.class);

        var turn2 = mock(Position.class);
        var move2 = mock(Move.class);
        when(turn2.lastMove()).thenReturn(move2);

        var finalHistory = history.add(new PositionMemento(turn1)).add(new PositionMemento(turn2));

        assertThat(finalHistory.lastMove()).isEqualTo(move2);
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(GameHistory.class).withNonnullFields("history").verify();
    }
}
