/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FenParserTest {

    @Test
    void nonAscii() {
        assertThatThrownBy(() -> FenParser.parse("♔♕♖♗♘♙♚♛♜♝♞♟"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("ASCII");
    }

    @Test
    void nonPrintableAscii() {
        assertThatThrownBy(() -> FenParser.parse("\u0000\u000f\u007f"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("ASCII");
    }

    @Test
    void extraOrMissingFields() {
        assertThatThrownBy(() -> FenParser.parse("- - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("6 data fields");
        assertThatThrownBy(() -> FenParser.parse("- - - - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("6 data fields");
    }

    @Test
    void illegalAllianceSymbol() {
        assertThatThrownBy(() -> FenParser.parse("- x - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("symbol")
                .hasMessageContaining("x");
    }

    @Test
    void badCasleAvailability() {
        assertThatThrownBy(() -> FenParser.parse("- w kqKQ - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("kqKQ");
    }

    @Test
    void illegalEnPassantTarget() {
        assertThatThrownBy(() -> FenParser.parse("- w - x9 - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("position")
                .hasMessageContaining("x9");
    }

    @Test
    void nonIntegerHalfmove() {
        assertThatThrownBy(() -> FenParser.parse("- w - - x -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("x");
        assertThatThrownBy(() -> FenParser.parse("- w - - 1.0 -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("1.0");
    }

    @Test
    void negativeHalfmove() {
        assertThatThrownBy(() -> FenParser.parse("- w - - -1 -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("negative")
                .hasMessageContaining("-1");
    }

    @Test
    void nonIntegerFullmove() {
        assertThatThrownBy(() -> FenParser.parse("- w - - 4 x"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("x");
        assertThatThrownBy(() -> FenParser.parse("- w - - 4 3.5"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("3.5");
    }

    @Test
    void negativeFullmove() {
        assertThatThrownBy(() -> FenParser.parse("- w - - 4 -3"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("negative")
                .hasMessageContaining("-3");
    }
}
