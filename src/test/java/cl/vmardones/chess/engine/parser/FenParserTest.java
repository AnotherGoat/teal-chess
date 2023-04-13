/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import cl.vmardones.chess.engine.piece.*;
import cl.vmardones.chess.engine.player.Alliance;
import org.junit.jupiter.api.Test;

class FenParserTest {

    @Test
    void nonAscii() {
        var input = "♔♕♖♗♘♙♚♛♜♝♞♟";
        assertThatThrownBy(() -> FenParser.parse(input))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("ASCII")
                .hasMessageContaining(input);
    }

    @Test
    void nonPrintableAscii() {
        var input = "\u0000\u000f\u007f";
        assertThatThrownBy(() -> FenParser.parse(input))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("ASCII")
                .hasMessageContaining(input);
    }

    @Test
    void extraOrMissingFields() {
        var fiveFields = "- - - - -";
        assertThatThrownBy(() -> FenParser.parse(fiveFields))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("6 data fields")
                .hasMessageContaining(fiveFields);

        var sevenFields = "- - - - - - - -";
        assertThatThrownBy(() -> FenParser.parse(sevenFields))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("6 data fields")
                .hasMessageContaining(sevenFields);
    }

    @Test
    void badPieceData() {
        assertThatThrownBy(() -> FenParser.parse("8/8/9/8/8/x/8/8 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("invalid characters")
                .hasMessageContaining("8/8/9/8/8/x/8");
    }

    @Test
    void missingKings() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/8 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("kings")
                .hasMessageContaining("7k/8/8/8/8/8/8");
        assertThatThrownBy(() -> FenParser.parse("8/8/8/8/8/8/8/7K - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("kings")
                .hasMessageContaining("8/8/8/8/8/8/7K");
    }

    @Test
    void tooManyKings() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/7k/8/8/8/8/7K - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("2 kings")
                .hasMessageContaining("7k/8/7k/8/8/8/8/7K");
    }

    @Test
    void extraOrMissingRanks() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/7K - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("8 ranks")
                .hasMessageContaining("7k/8/8/8/8/8/7K");
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/8/7K - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("8 ranks")
                .hasMessageContaining("7k/8/8/8/8/8/8/8/7K");
    }

    @Test
    void illegalAllianceSymbol() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K x - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("symbol")
                .hasMessageContaining("x");
    }

    @Test
    void badCasleAvailability() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w kqKQ - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("kqKQ");
    }

    @Test
    void illegalEnPassantTarget() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w - x9 - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("position")
                .hasMessageContaining("x9");
    }

    @Test
    void nonIntegerHalfmove() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w - - x -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("x");
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w - - 1.0 -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("1.0");
    }

    @Test
    void negativeHalfmove() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w - - -1 -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("negative")
                .hasMessageContaining("-1");
    }

    @Test
    void nonIntegerFullmove() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w - - 4 x"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("x");
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w - - 4 3.5"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("3.5");
    }

    @Test
    void negativeFullmove() {
        assertThatThrownBy(() -> FenParser.parse("7k/8/8/8/8/8/8/7K w - - 4 -3"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("negative")
                .hasMessageContaining("-3");
    }

    @Test
    void buildBoard() {
        // Board from the explained Chess.com example at https://www.chess.com/terms/fen-chess
        var board = FenParser.parse("r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1 w - - 0 1");

        assertThat(board.pieceAt("a1")).isEqualTo(new Queen("a1", Alliance.BLACK));
        assertThat(board.pieceAt("b5")).isEqualTo(new Pawn("b5", Alliance.BLACK));
        assertThat(board.pieceAt("c2")).isEqualTo(new Pawn("c2", Alliance.WHITE));
        assertThat(board.pieceAt("d5")).isEqualTo(new Knight("d5", Alliance.WHITE));
        assertThat(board.pieceAt("e2")).isEqualTo(new King("e2", Alliance.WHITE));
        assertThat(board.pieceAt("f6")).isEqualTo(new Knight("f6", Alliance.BLACK));
        assertThat(board.pieceAt("g1")).isEqualTo(new Bishop("g1", Alliance.BLACK));
        assertThat(board.pieceAt("h8")).isEqualTo(new Rook("h8", Alliance.BLACK));

        assertThat(board.pieceAt("a3")).isNull();
        assertThat(board.pieceAt("b7")).isNull();
        assertThat(board.pieceAt("c4")).isNull();
        assertThat(board.pieceAt("d1")).isNull();
        assertThat(board.pieceAt("e6")).isNull();
        assertThat(board.pieceAt("f5")).isNull();
        assertThat(board.pieceAt("g8")).isNull();
        assertThat(board.pieceAt("h2")).isNull();
    }
}
