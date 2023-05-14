/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.fen;

import static org.assertj.core.api.Assertions.assertThat;

import com.vmardones.tealchess.board.Board;
import org.junit.jupiter.api.Test;

final class FenParserTest {

    @Test
    void parseInitialBoard() {
        var initialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertThat(FenParser.parse(initialFen).board()).isEqualTo(Board.INITIAL_BOARD);
    }
}
