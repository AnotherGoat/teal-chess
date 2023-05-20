/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.board;

import static com.vmardones.tealchess.board.Board.INITIAL_BOARD;
import static com.vmardones.tealchess.board.Mailbox.INITIAL_MAILBOX;
import static com.vmardones.tealchess.color.Color.BLACK;
import static com.vmardones.tealchess.color.Color.WHITE;
import static com.vmardones.tealchess.piece.PieceType.*;
import static com.vmardones.tealchess.square.Square.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.stream.IntStream;

import com.vmardones.tealchess.board.Board.BoardBuilder;
import com.vmardones.tealchess.piece.Piece;
import com.vmardones.tealchess.square.Square;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class BoardTest {

    Board board = Board.builder(e1, e8).build();
    BoardBuilder builder;

    @BeforeEach
    void setUp() {
        builder = Board.builder(e1, e8);
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Board.class).verify();
    }

    @Test
    void initialWhitePawns() {
        var expectedBitboard = 0b11111111_00000000L;
        assertThat(INITIAL_BOARD.pawns(WHITE)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackPawns() {
        var expectedBitboard = 0b11111111_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(INITIAL_BOARD.pawns(BLACK)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteKnights() {
        var expectedBitboard = 0b01000010L;
        assertThat(INITIAL_BOARD.knights(WHITE)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackKnights() {
        var expectedBitboard = 0b01000010_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(INITIAL_BOARD.knights(BLACK)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteBishops() {
        var expectedBitboard = 0b00100100L;
        assertThat(INITIAL_BOARD.bishops(WHITE)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackBishops() {
        var expectedBitboard = 0b00100100_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(INITIAL_BOARD.bishops(BLACK)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteRooks() {
        var expectedBitboard = 0b10000001L;
        assertThat(INITIAL_BOARD.rooks(WHITE)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackRooks() {
        var expectedBitboard = 0b10000001_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(INITIAL_BOARD.rooks(BLACK)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteQueen() {
        var expectedBitboard = 0b00001000L;
        assertThat(INITIAL_BOARD.queens(WHITE)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackQueen() {
        var expectedBitboard = 0b00001000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(INITIAL_BOARD.queens(BLACK)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialWhiteKing() {
        var expectedBitboard = 0b00010000L;
        assertThat(INITIAL_BOARD.kings(WHITE)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialBlackKing() {
        var expectedBitboard = 0b00010000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertThat(INITIAL_BOARD.kings(BLACK)).isEqualTo(expectedBitboard);
    }

    @Test
    void initialEmptySquares() {
        IntStream.range(16, 48)
                .forEach(square -> assertThat(INITIAL_MAILBOX.pieceAt(square)).isNull());
    }

    @Test
    void initialWhitePawnRank() {
        IntStream.range(8, 16).forEach(square -> assertThat(INITIAL_MAILBOX.pieceAt(square))
                .isEqualTo(new Piece(PAWN, WHITE, square)));
    }

    @Test
    void initialBlackPawnRank() {
        IntStream.range(48, 56).forEach(square -> assertThat(INITIAL_MAILBOX.pieceAt(square))
                .isEqualTo(new Piece(PAWN, BLACK, square)));
    }

    @Test
    void initialWhiteBackRank() {
        assertThat(INITIAL_MAILBOX.pieceAt(a1)).isEqualTo(new Piece(ROOK, WHITE, a1));
        assertThat(INITIAL_MAILBOX.pieceAt(b1)).isEqualTo(new Piece(KNIGHT, WHITE, b1));
        assertThat(INITIAL_MAILBOX.pieceAt(c1)).isEqualTo(new Piece(BISHOP, WHITE, c1));
        assertThat(INITIAL_MAILBOX.pieceAt(d1)).isEqualTo(new Piece(QUEEN, WHITE, d1));
        assertThat(INITIAL_MAILBOX.pieceAt(e1)).isEqualTo(new Piece(KING, WHITE, e1));
        assertThat(INITIAL_MAILBOX.pieceAt(f1)).isEqualTo(new Piece(BISHOP, WHITE, f1));
        assertThat(INITIAL_MAILBOX.pieceAt(g1)).isEqualTo(new Piece(KNIGHT, WHITE, g1));
        assertThat(INITIAL_MAILBOX.pieceAt(h1)).isEqualTo(new Piece(ROOK, WHITE, h1));
    }

    @Test
    void initialBlackBackRank() {
        assertThat(INITIAL_MAILBOX.pieceAt(a8)).isEqualTo(new Piece(ROOK, BLACK, a8));
        assertThat(INITIAL_MAILBOX.pieceAt(b8)).isEqualTo(new Piece(KNIGHT, BLACK, b8));
        assertThat(INITIAL_MAILBOX.pieceAt(c8)).isEqualTo(new Piece(BISHOP, BLACK, c8));
        assertThat(INITIAL_MAILBOX.pieceAt(d8)).isEqualTo(new Piece(QUEEN, BLACK, d8));
        assertThat(INITIAL_MAILBOX.pieceAt(e8)).isEqualTo(new Piece(KING, BLACK, e8));
        assertThat(INITIAL_MAILBOX.pieceAt(f8)).isEqualTo(new Piece(BISHOP, BLACK, f8));
        assertThat(INITIAL_MAILBOX.pieceAt(g8)).isEqualTo(new Piece(KNIGHT, BLACK, g8));
        assertThat(INITIAL_MAILBOX.pieceAt(h8)).isEqualTo(new Piece(ROOK, BLACK, h8));
    }

    @Test
    void isEmpty() {
        var mailbox = new Mailbox(board);

        assertThat(mailbox.isEmpty(a1)).isTrue();
        assertThat(mailbox.isEmpty(h8)).isTrue();
    }

    @Test
    void getWhiteColor() {
        assertThat(board.colorOf(a8)).isEqualTo(WHITE);
        assertThat(board.colorOf(h1)).isEqualTo(WHITE);
    }

    @Test
    void getBlackColor() {
        assertThat(board.colorOf(b8)).isEqualTo(BLACK);
        assertThat(board.colorOf(g1)).isEqualTo(BLACK);
    }

    @Test
    void whitePieceUnicode() {
        assertThat(board.squareAsUnicode(e1)).isEqualTo("♔");
    }

    @Test
    void blackPieceUnicode() {
        assertThat(board.squareAsUnicode(e8)).isEqualTo("♚");
    }

    @Test
    void emptySquareUnicode() {
        assertThat(board.squareAsUnicode(g4)).isEqualTo("□");
        assertThat(board.squareAsUnicode(h4)).isEqualTo("■");
    }

    @Test
    void addPiece() {
        var piece = new Piece(QUEEN, WHITE, d7);
        var newBoard = builder.with(piece).build();
        var mailbox = new Mailbox(newBoard);

        assertThat(mailbox.pieceAt(d7)).isEqualTo(piece);
    }

    @Test
    void addNullPiece() {
        var board1 = builder.build();
        var board2 = builder.with(null).build();

        assertThat(board1).isEqualTo(board2);
    }

    @Test
    void lastPieceTakesPrecedence() {
        var piece1 = new Piece(PAWN, WHITE, a1);
        var piece2 = new Piece(ROOK, WHITE, Square.a1);
        var newBoard = builder.with(piece1).with(piece2).build();
        var mailbox = new Mailbox(newBoard);

        assertThat(mailbox.pieceAt(Square.a1)).isEqualTo(piece2);
    }

    @Test
    void withoutPiece() {
        var piece = new Piece(PAWN, BLACK, a5);
        var newBoard = builder.with(piece).without(a5).build();
        var mailbox = new Mailbox(newBoard);

        assertThat(mailbox.pieceAt(a5)).isNull();
    }

    @Test
    void withoutNonExistantPiece() {
        var board1 = builder.build();
        var board2 = builder.without(c6).build();

        assertThat(board1).isEqualTo(board2);
    }

    @Test
    void alwaysAddsWhiteKing() {
        var whiteKing = new Piece(KING, WHITE, e1);
        var impostorQueen = new Piece(QUEEN, WHITE, e1);
        var newBoard = builder.with(impostorQueen).build();
        var mailbox = new Mailbox(newBoard);

        assertThat(mailbox.pieceAt(e1)).isNotEqualTo(impostorQueen).isEqualTo(whiteKing);
    }

    @Test
    void alwaysAddsBlackKing() {
        var blackKing = new Piece(KING, BLACK, e8);
        var impostorQueen = new Piece(QUEEN, BLACK, e8);
        var newBoard = builder.with(impostorQueen).build();
        var mailbox = new Mailbox(newBoard);

        assertThat(mailbox.pieceAt(e8)).isNotEqualTo(impostorQueen).isEqualTo(blackKing);
    }

    // Move some of these tests to "mailbox test"
    @Test
    void unmodifiablePieces() {
        var pieces = new Mailbox(board).pieces();
        assertThatThrownBy(pieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unicode() {
        assertThat(INITIAL_BOARD.unicode())
                .containsOnlyOnce("♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜")
                .containsOnlyOnce("♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟")
                .contains("□ ■ □ ■ □ ■ □ ■")
                .contains("■ □ ■ □ ■ □ ■ □")
                .containsOnlyOnce("♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙")
                .containsOnlyOnce("♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖")
                .contains("\n");
    }
}
