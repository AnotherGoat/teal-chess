/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.vmardones.chess.engine.parser.FenParser;
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
        var position = Position.INITIAL_POSITION;
        gameState.currentPosition(position);

        assertThat(gameState.save().state()).isEqualTo(position);
    }

    @Test
    void saveNull() {
        assertThatThrownBy(gameState::save)
                .isInstanceOf(IllegalSaveException.class)
                .hasMessageContaining("null");
    }

    @Test
    void load() {
        var firstPosition = Position.INITIAL_POSITION;
        gameState.currentPosition(firstPosition);
        var firstMemento = gameState.save();

        var secondPosition = FenParser.parse("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
        gameState.currentPosition(secondPosition);

        gameState.load(firstMemento);

        assertThat(gameState.currentPosition()).isEqualTo(firstPosition).isNotEqualTo(secondPosition);
    }
}
