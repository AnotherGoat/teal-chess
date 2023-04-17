/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.engine.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.vmardones.chess.engine.piece.*;
import cl.vmardones.chess.engine.player.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

    Board.BoardBuilder builder;
    King whiteKing = new King("e1", Color.WHITE);
    King blackKing = new King("e8", Color.BLACK);

    @BeforeEach
    void setUp() {
        builder = Board.builder(whiteKing, blackKing);
    }

    @Test
    void contains() {
        var piece = new Bishop("e2", Color.WHITE);
        var board = builder.with(piece).build();

        assertThat(board.contains("e2", Bishop.class)).isTrue();
    }

    @Test
    void isEmpty() {
        var board = builder.build();

        assertThat(board.isEmpty(Coordinate.of("a1"))).isTrue();
        assertThat(board.isEmpty(Coordinate.of("h8"))).isTrue();
    }

    @Test
    void nextPositionBuilder() {
        var board = builder.build();
        var nextPositionBoard = board.nextPositionBuilder().build();

        assertThat(board.king(Color.WHITE)).isEqualTo(nextPositionBoard.king(Color.WHITE));
        assertThat(board.king(Color.BLACK)).isEqualTo(nextPositionBoard.king(Color.BLACK));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Board.class)
                .withNonnullFields("squares", "whiteKing", "whitePieces", "blackKing", "blackPieces")
                .verify();
    }

    @Test
    void addPiece() {
        var piece = new Queen("d7", Color.WHITE);
        var board = builder.with(piece).build();

        assertThat(board.pieces(Color.WHITE)).containsOnlyOnce(piece);
    }

    @Test
    void addNullPiece() {
        var board = builder.build();
        var nextPositionBoard = board.nextPositionBuilder().with(null).build();

        assertThat(board).isEqualTo(nextPositionBoard);
    }

    @Test
    void lastPieceTakesPrecedence() {
        var firstPiece = new Pawn("a1", Color.WHITE);
        var secondPiece = new Rook("a1", Color.WHITE);
        var board = builder.with(firstPiece).with(secondPiece).build();

        assertThat(board.pieces(Color.WHITE)).doesNotContain(firstPiece).containsOnlyOnce(secondPiece);
    }

    @Test
    void withoutPiece() {
        var piece = new Pawn("a5", Color.BLACK);
        var board = builder.with(piece).without(piece).build();

        assertThat(board.pieces(Color.BLACK)).isNotEmpty().doesNotContain(piece);
    }

    @Test
    void withoutNullPiece() {
        var board = builder.build();
        var nextPositionBoard = board.nextPositionBuilder().without(null).build();

        assertThat(board).isEqualTo(nextPositionBoard);
    }

    @Test
    void alwaysAddsWhiteKing() {
        var newWhiteKing = new King("h8", Color.WHITE);
        var impostorQueen = new Queen("h8", Color.WHITE);

        var board = Board.builder(newWhiteKing, blackKing).with(impostorQueen).build();

        assertThat(board.pieces(Color.WHITE)).doesNotContain(impostorQueen).containsOnlyOnce(newWhiteKing);
    }

    @Test
    void alwaysAddsBlackKing() {
        var newBlackKing = new King("a1", Color.BLACK);
        var impostorQueen = new Queen("a1", Color.BLACK);

        var board = Board.builder(whiteKing, newBlackKing).with(impostorQueen).build();

        assertThat(board.pieces(Color.BLACK)).doesNotContain(impostorQueen).containsOnlyOnce(newBlackKing);
    }

    @Test
    void unmodifiableSquares() {
        var squares = BoardDirector.createStandardBoard().squares();

        assertThatThrownBy(squares::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableWhitePieces() {
        var whitePieces = BoardDirector.createStandardBoard().pieces(Color.WHITE);

        assertThatThrownBy(whitePieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void unmodifiableBlackPieces() {
        var blackPieces = BoardDirector.createStandardBoard().pieces(Color.BLACK);

        assertThatThrownBy(blackPieces::clear).isInstanceOf(UnsupportedOperationException.class);
    }
}
