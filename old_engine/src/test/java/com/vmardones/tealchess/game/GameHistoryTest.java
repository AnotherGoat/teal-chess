/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vmardones.tealchess.move.LegalMove;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

final class GameHistoryTest {

    @Test
    void addToHistory() {
        var memento1 = mock(GameMemento.class);
        var history1 = new GameHistory(memento1);

        var memento2 = mock(GameMemento.class);
        var history2 = history1.add(memento2);

        assertThat(history2).isNotSameAs(history1);
    }

    @Test
    void moveHistory() {
        var memento1 = mock(GameMemento.class);
        var history1 = new GameHistory(memento1);

        var memento2 = mock(GameMemento.class);
        var history2 = history1.add(memento2);

        when(memento2.lastMove()).thenReturn(mock(LegalMove.class));

        assertThat(history1.moves()).isEmpty();
        assertThat(history2.moves()).isNotSameAs(history1.moves()).hasSize(1);
    }

    @Test
    void unmodifiableMoves() {
        var moves = new GameHistory(mock(GameMemento.class)).moves();

        assertThatThrownBy(moves::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void lastMove() {
        var memento1 = mock(GameMemento.class);
        var history1 = new GameHistory(memento1);

        var memento2 = mock(GameMemento.class);
        var history2 = history1.add(memento2);

        when(memento1.lastMove()).thenReturn(null);
        var move = mock(LegalMove.class);
        when(memento2.lastMove()).thenReturn(move);

        assertThat(history1.lastMove()).isNull();
        assertThat(history2.lastMove()).isEqualTo(move);
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(GameHistory.class).withNonnullFields("history").verify();
    }
}
