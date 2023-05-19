/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.game;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.ExcludeFromNullAway;
import com.vmardones.tealchess.parser.fen.FenParser;
import org.junit.jupiter.api.Test;

@ExcludeFromNullAway
final class GameStateTest {

    @Test
    void save() {
        var gameState = new GameState();

        assertThat(gameState.save().position()).isEqualTo(Position.INITIAL_POSITION);
    }

    @Test
    void load() {
        var gameState = new GameState();
        var firstMemento = gameState.save();

        var secondPosition = FenParser.parse("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
        gameState.position(secondPosition);

        gameState.load(firstMemento);

        assertThat(gameState.position()).isEqualTo(firstMemento.position()).isNotEqualTo(secondPosition);
    }
}
