/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Test
    void createStandardBoard() {
        var board = new BoardService().createStandardBoard();

        assertThat(board.toString())
                .containsOnlyOnce("r  n  b  q  k  b  n  r")
                .containsOnlyOnce("p  p  p  p  p  p  p  p")
                .contains("-  -  -  -  -  -  -  -")
                .containsOnlyOnce("P  P  P  P  P  P  P  P")
                .containsOnlyOnce("R  N  B  Q  K  B  N  R")
                .contains("\n");
    }

    @Test
    void cache() {
        var firstBoard = new BoardService().createStandardBoard();
        var secondBoard = new BoardService().createStandardBoard();

        assertThat(firstBoard).isSameAs(secondBoard);
    }
}
