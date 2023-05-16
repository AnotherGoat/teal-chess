/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.parser.fen;

import static com.vmardones.tealchess.color.Color.BLACK;
import static com.vmardones.tealchess.color.Color.WHITE;
import static com.vmardones.tealchess.piece.PieceType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.vmardones.tealchess.board.Board;
import com.vmardones.tealchess.piece.Piece;
import org.junit.jupiter.api.Test;

final class FenParserTest {

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
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/8 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("kings")
                .hasMessageContaining("4k3/8/8/8/8/8/8");
        assertThatThrownBy(() -> FenParser.parse("8/8/8/8/8/8/8/4K3 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("kings")
                .hasMessageContaining("8/8/8/8/8/8/4K3");
    }

    @Test
    void tooManyKings() {
        // 2 white kings
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/4K3/8/4K3 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("2 kings")
                .hasMessageContaining("4k3/8/8/8/8/4K3/8/4K3");

        // 2 black kings
        assertThatThrownBy(() -> FenParser.parse("4k3/8/4k3/8/8/8/8/4K3 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("2 kings")
                .hasMessageContaining("4k3/8/4k3/8/8/8/8/4K3");
    }

    @Test
    void extraOrMissingRanks() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/4K3 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("8 ranks")
                .hasMessageContaining("4k3/8/8/8/8/8/4K3");
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/8/4K3 - - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("8 ranks")
                .hasMessageContaining("4k3/8/8/8/8/8/8/8/4K3");
    }

    @Test
    void illegalColorSymbol() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 x - - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("symbol")
                .hasMessageContaining("x");
    }

    @Test
    void badCasleAvailability() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w kqKQ - - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("kqKQ");
    }

    @Test
    void illegalEnPassantTarget() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - x9 - -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("square")
                .hasMessageContaining("x9");
    }

    @Test
    void nonIntegerHalfmove() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - x -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("x");
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 1.0 -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("1.0");
    }

    @Test
    void negativeHalfmove() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - -1 -"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("negative")
                .hasMessageContaining("-1");
    }

    @Test
    void nonIntegerFullmove() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 4 x"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("x");
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 4 3.5"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("integer")
                .hasMessageContaining("3.5");
    }

    @Test
    void negativeFullmove() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 4 -3"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("negative")
                .hasMessageContaining("-3");
    }

    @Test
    void zeroFullmove() {
        assertThatThrownBy(() -> FenParser.parse("4k3/8/8/8/8/8/8/4K3 w - - 4 0"))
                .isInstanceOf(FenParseException.class)
                .hasMessageContaining("zero")
                .hasMessageContaining("0");
    }

    @Test
    void parseInitialBoard() {
        var initialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertThat(FenParser.parse(initialFen).board()).isEqualTo(Board.INITIAL_BOARD);
    }

    @Test
    void buildBoard() {
        // Board from the explained Chess.com example at https://www.chess.com/terms/fen-chess
        var board = FenParser.parse("r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1 w - - 0 1")
                .board();

        assertThat(board.pieceAt(a1)).isEqualTo(new Piece(QUEEN, BLACK, a1));
        assertThat(board.pieceAt(b5)).isEqualTo(new Piece(PAWN, BLACK, b5));
        assertThat(board.pieceAt(c2)).isEqualTo(new Piece(PAWN, WHITE, c2));
        assertThat(board.pieceAt(d5)).isEqualTo(new Piece(KNIGHT, WHITE, d5));
        assertThat(board.pieceAt(e2)).isEqualTo(new Piece(KING, WHITE, e2));
        assertThat(board.pieceAt(f6)).isEqualTo(new Piece(KNIGHT, BLACK, f6));
        assertThat(board.pieceAt(g1)).isEqualTo(new Piece(BISHOP, BLACK, g1));
        assertThat(board.pieceAt(h8)).isEqualTo(new Piece(ROOK, BLACK, h8));

        assertThat(board.pieceAt(a3)).isNull();
        assertThat(board.pieceAt(b7)).isNull();
        assertThat(board.pieceAt(c4)).isNull();
        assertThat(board.pieceAt(d1)).isNull();
        assertThat(board.pieceAt(e6)).isNull();
        assertThat(board.pieceAt(f5)).isNull();
        assertThat(board.pieceAt(g8)).isNull();
        assertThat(board.pieceAt(h2)).isNull();
    }
}
