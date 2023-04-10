/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameStateTest {

    GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    void save() {
        var firstTurn = mock(Turn.class);
        gameState.currentTurn(firstTurn);

        assertThat(gameState.save().state()).isEqualTo(firstTurn);
    }

    @Test
    void saveNull() {
        assertThatThrownBy(gameState::save)
                .isInstanceOf(IllegalSaveException.class)
                .hasMessageContaining("null");
    }

    @Test
    void load() {
        var firstTurn = mock(Turn.class);
        gameState.currentTurn(firstTurn);
        var firstMemento = gameState.save();

        var secondTurn = mock(Turn.class);
        gameState.currentTurn(secondTurn);

        gameState.load(firstMemento);

        assertThat(gameState.currentTurn()).isEqualTo(firstTurn).isNotEqualTo(secondTurn);
    }
}
