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
        var initialPosition = mock(Position.class);
        gameState.currentPosition(initialPosition);

        assertThat(gameState.save().state()).isEqualTo(initialPosition);
    }

    @Test
    void saveNull() {
        assertThatThrownBy(gameState::save)
                .isInstanceOf(IllegalSaveException.class)
                .hasMessageContaining("null");
    }

    @Test
    void load() {
        var firstPosition = mock(Position.class);
        gameState.currentPosition(firstPosition);
        var firstMemento = gameState.save();

        var secondPosition = mock(Position.class);
        gameState.currentPosition(secondPosition);

        gameState.load(firstMemento);

        assertThat(gameState.currentPosition()).isEqualTo(firstPosition).isNotEqualTo(secondPosition);
    }
}
