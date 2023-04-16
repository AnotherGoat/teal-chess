/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BoardDirectorTest {

    @Test
    void createStandardBoard() {
        var board = BoardDirector.createStandardBoard();

        assertThat(board.toString())
                .containsOnlyOnce("♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜")
                .containsOnlyOnce("♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟")
                .contains("□ ■ □ ■ □ ■ □ ■")
                .contains("■ □ ■ □ ■ □ ■ □")
                .containsOnlyOnce("♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙")
                .containsOnlyOnce("♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖")
                .contains("\n");
    }

    @Test
    void cache() {
        var firstBoard = BoardDirector.createStandardBoard();
        var secondBoard = BoardDirector.createStandardBoard();

        assertThat(firstBoard).isSameAs(secondBoard);
    }
}
