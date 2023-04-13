/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FenParserTest {

    @Test
    void rejectNonAscii() {
        assertThatThrownBy(() -> FenParser.parse("♔♕♖♗♘♙♚♛♜♝♞♟")).isInstanceOf(FenParseException.class);
    }
}
